package cafegaza.cafegazaspring.repository;

import cafegaza.cafegazaspring.controller.SearchQuery;
import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.domain.QMenu;
import cafegaza.cafegazaspring.domain.QOpenHour;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalTime;
import java.util.*;

import static cafegaza.cafegazaspring.domain.QCafe.cafe;

@RequiredArgsConstructor
public class CafeRepositoryImpl implements CafeRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    QMenu menu = QMenu.menu;
    QOpenHour openHour = QOpenHour.openHour;

    /**
     * 다중 조건으로 카페 검색하기
     */
    @Override
    public Page<Cafe> findByMultipleCond(String region, String keyword, double[] centerCoord, SearchQuery searchQuery, Pageable pageable) {

        List<Cafe> cafeList =  queryFactory
                .select(cafe).distinct()
                .from(cafe)
                .leftJoin(menu).on(cafe.eq(menu.cafe))
                .leftJoin(openHour).on(cafe.eq(openHour.cafe))
                .where(regionContain(region)
                        , withinRadius(centerCoord)
                        , keywordContain(keyword)
                        , menuContain(searchQuery.getMenuOption())
                        , openHourCompare(searchQuery.getStartHour(), searchQuery.getEndHour())
                        , isCurrentOpen(searchQuery.getCurrentOpen())
                        , isDawnOpen(searchQuery.getDawnOpen())
                )
                .groupBy(cafe.cafeId)
                .having(priceLowerThan(searchQuery.getMaxPrice()))
                .orderBy(cafeSort(searchQuery.getSortType()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        // 페이징을 적용하지 않은 전체 결과의 크기 쿼리
//        JPAQuery<Long> count = queryFactory
//                .select(cafe.cafeId.countDistinct()) // count()-> 중복 제거가 안됨 countDistinct 로 pk 중복을 제거
//                .from(cafe)
//                .leftJoin(menu).on(cafe.eq(menu.cafe))
//                .where(regionContain(region), withinRadius(centerCoord), keywordContain(keyword), menuContain(searchQuery.getMenuOption()))
//                .groupBy(cafe.cafeId)
//                .having(priceLowerThan(searchQuery.getMaxPrice()));

        //        return PageableExecutionUtils.getPage(cafeList, pageable, count::fetchOne);

        // group by 추가 이후 countDistinct() 에서 totalElements 수가 맞게 찾아지지 않는 오류 발생, 아직 해결하지 못함
        // 대신 count() 를 사용했을 땐 fetchOne 에서 NonUniqueResultException 에러 발생 -> 이것도 이유를 아직 못찾음
        // fetch().size() 를 사용한 int 타입의 count 값을 구하여 페이징에 사용할 수 있도록 일단 PageImpl 리턴 방식으로 변경

        int totalCount = queryFactory
                .select(cafe).distinct() // count()-> 중복 제거가 안됨 countDistinct 로 pk 중복을 제거
                .from(cafe)
                .leftJoin(menu).on(cafe.eq(menu.cafe))
                .leftJoin(openHour).on(cafe.eq(openHour.cafe))
                .where(regionContain(region)
                        , withinRadius(centerCoord)
                        , keywordContain(keyword)
                        , menuContain(searchQuery.getMenuOption())
                        , openHourCompare(searchQuery.getStartHour(), searchQuery.getEndHour())
                        , isCurrentOpen(searchQuery.getCurrentOpen())
                        , isDawnOpen(searchQuery.getDawnOpen())
                )
                .groupBy(cafe.cafeId)
                .having(priceLowerThan(searchQuery.getMaxPrice()))
                .fetch().size();

        return new PageImpl<Cafe>(cafeList, pageable, totalCount);
    }

    // 행정구역명으로 찾기
    private BooleanExpression regionContain(String region) {
        return region == null ? null : cafe.address.contains(region).or(cafe.roadAddress.contains(region));
    }
    // 반경 1km 내 카페 찾기
    private BooleanExpression withinRadius(double[] centerCoord) {
        if(centerCoord == null) {
            return null;
        }
        // template 을 사용하여 직접 sql 문 작성
        NumberTemplate distance = Expressions.numberTemplate(Double.class, "6371*acos(cos(radians({0}))*cos(radians({1}))*cos(radians({2})-radians({3}))+sin(radians({4}))*sin(radians({5})))",
                centerCoord[1], cafe.y, cafe.x, centerCoord[0], centerCoord[1], cafe.y); // 기준 좌표와 카페까지의 거리 계산

        return distance.lt(1);
    }

    // 특정 키워드명(카페명)으로 찾기
    private BooleanExpression keywordContain(String keyword) {
        return keyword.isEmpty() ? null : cafe.name.contains(keyword);
    }

    // 특정 메뉴가 있는 카페 찾기
    private BooleanExpression menuContain(String menuName) {
        return menuName.isEmpty() ? null : menu.menuName.contains(menuName);
    }

    // 평균 가격이 더 낮은 카페 찾기
    private BooleanExpression priceLowerThan(int maxPrice) {
        if(maxPrice == 0) {
            return null;
        }
        NumberExpression<Double> priceAvg = menu.price.avg();
        return priceAvg.gt(0).and(priceAvg.lt(maxPrice));
    }

    // start ~ end 시간 사이에 영업 중인 카페 찾기
    private BooleanExpression openHourCompare(int startHour, int endHour) {

        if(startHour == 0 && endHour == 0) {
            return null;
        }
        // 분 단위로 바꿈
        startHour = startHour * 60;
        endHour = endHour * 60;
        // 현재 요일에 카페의 영업 시간이 지정한 start, end 시간 안에 부분 포함되는지 확인
        return openHour.day.eq(getLocalDay())
                .and(openHour.startTime.lt(startHour).or(openHour.startTime.lt(endHour)))
                .and(openHour.endTime.gt(endHour).or(openHour.endTime.gt(startHour)));
    }

    // 현재 영업 중인 가게 찾기
    private BooleanExpression isCurrentOpen(int currentOpen) {
        if (currentOpen == 0){
            return null;
        }
        LocalTime now = LocalTime.now(); // 현재 시간 구하기
        int searchTime = (now.getHour() * 60) + now.getMinute(); // 분 단위로 바꿔서 검색
        return openHour.day.eq(getLocalDay())
                .and(openHour.startTime.lt(searchTime).and(openHour.endTime.gt(searchTime)));
    }
    // 새벽 운영하는 가게 찾기
    private BooleanExpression isDawnOpen(int dawnOpen) {
        if (dawnOpen == 0) {
            return null;
        }
        // 영업 종료 시간이 새벽 1시 이후거나 영업 시작 시간이 자정 ~ 새벽 4시 사이일 때
        return openHour.day.eq(getLocalDay())
                .and(openHour.endTime.goe(60).and(openHour.endTime.lt(openHour.startTime)).or(openHour.startTime.goe(0).and(openHour.startTime.loe(240))));
    }

    // 정렬 옵션에 맞는 동적 정렬
    public OrderSpecifier cafeSort(int sortType) {
        if(sortType == 1){
            return new OrderSpecifier(Order.DESC, cafe.rate);
        } else {
            return new OrderSpecifier(Order.DESC, cafe.reviewCount);
        }
    }

    // 현재 요일 가져오기
    private String getLocalDay() {
        List<String> list = Arrays.asList("일", "월", "화", "수", "목", "금", "토", "일");
        Calendar cal = Calendar.getInstance();
        String dayOfWeek = list.get(cal.get(Calendar.DAY_OF_WEEK) - 1); // 현재 요일 구하기, 1~7 -> 일, 월~금, 토
        return dayOfWeek;
    }


}
