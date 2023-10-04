package cafegaza.cafegazaspring.repository;

import cafegaza.cafegazaspring.controller.SearchQuery;
import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.domain.QMenu;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static cafegaza.cafegazaspring.domain.QCafe.cafe;

@RequiredArgsConstructor
public class CafeRepositoryImpl implements CafeRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    QMenu menu = QMenu.menu;

    /**
     * 다중 조건으로 카페 검색하기
     */
    @Override
    public Page<Cafe> findByMultipleCond(String region, String keyword, double[] centerCoord, SearchQuery searchQuery, Pageable pageable) {

        List<Cafe> cafeList =  queryFactory
                .select(cafe)
                .from(cafe).distinct()
                .leftJoin(menu).on(cafe.eq(menu.cafe))
                .where(regionContain(region)
                        , withinRadius(centerCoord)
                        , keywordContain(keyword)
                        , menuContain(searchQuery.getMenuOption()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 페이징을 적용하지 않은 전체 결과의 크기 쿼리
        JPAQuery<Long> count = queryFactory
                .select(cafe.countDistinct()) // count()-> 중복 제거가 안됨 countDistinct 로 pk 중복을 제거
                .from(cafe)
                .leftJoin(menu).on(cafe.eq(menu.cafe))
                .where(regionContain(region), withinRadius(centerCoord), keywordContain(keyword), menuContain(searchQuery.getMenuOption()));

        return PageableExecutionUtils.getPage(cafeList, pageable, count::fetchOne);
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
}
