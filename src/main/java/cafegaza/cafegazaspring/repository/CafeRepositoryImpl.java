package cafegaza.cafegazaspring.repository;

import cafegaza.cafegazaspring.controller.SearchQuery;
import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.domain.QMenu;
import com.querydsl.core.types.dsl.BooleanExpression;
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
     * 지역명 + 키워드 검색
     */
    @Override
    public Page<Cafe> findByRegionKeyword(String region, String keyword, Pageable pageable) {
        // 페이징을 사용하여 데이터 조회
        List<Cafe> cafeList =  queryFactory
                .selectFrom(cafe)
                .where(cafe.name.contains(keyword)
                        .andAnyOf(cafe.address.contains(region).or(cafe.roadAddress.contains(region))))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        // 페이징을 적용하지 않은 전체 결과의 크기 쿼리
        JPAQuery<Long> count = queryFactory
                .select(cafe.count())
                .from(cafe)
                .where(cafe.name.contains(keyword)
                        .andAnyOf(cafe.address.contains(region).or(cafe.roadAddress.contains(region))));

        return PageableExecutionUtils.getPage(cafeList, pageable, count::fetchOne);

    }

    /**
     * 지역명 + 메뉴명 검색
     */

    @Override
    public Page<Cafe> findByRegionMenu(String region, String menuName, Pageable pageable) {
        // 페이징을 사용하여 데이터 조회
        List<Cafe> cafeList =  queryFactory
                .select(cafe)
                .from(cafe).distinct()
                .leftJoin(menu).on(cafe.eq(menu.cafe)) // 해당 메뉴가 있는 카페일 때
                .where(menu.menuName.contains(menuName)
                        .andAnyOf(cafe.address.contains(region).or(cafe.roadAddress.contains(region))))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 페이징을 적용하지 않은 전체 결과의 크기 쿼리
        JPAQuery<Long> count = queryFactory
                .select(cafe.countDistinct()) // count()-> 중복 제거가 안됨 countDistinct 로 pk 중복을 제거
                .from(cafe)
                .leftJoin(menu).on(cafe.eq(menu.cafe)) // 해당 메뉴가 있는 카페일 때
                .where(menu.menuName.contains(menuName).andAnyOf(cafe.address.contains(region).or(cafe.roadAddress.contains(region))));


        return PageableExecutionUtils.getPage(cafeList, pageable, count::fetchOne);
    }

    /**
     * 지역명 + 키워드 + 메뉴명 검색
     */
    public Page<Cafe> findByRegionkeywordMenu(String region, String keyword, String menuName, Pageable pageable) {

        List<Cafe> cafeList =  queryFactory
                .select(cafe)
                .from(cafe).distinct()
                .leftJoin(menu).on(cafe.eq(menu.cafe)) // 해당 메뉴가 있는 카페일 때
                .where(menu.menuName.contains(menuName)
                        .and(cafe.name.contains(keyword))
                        .andAnyOf(cafe.address.contains(region).or(cafe.roadAddress.contains(region))))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 페이징을 적용하지 않은 전체 결과의 크기 쿼리
        JPAQuery<Long> count = queryFactory
                .select(cafe.countDistinct()) // count()-> 중복 제거가 안됨 countDistinct 로 pk 중복을 제거
                .from(cafe)
                .leftJoin(menu).on(cafe.eq(menu.cafe)) // 해당 메뉴가 있는 카페일 때
                .where(menu.menuName.contains(menuName)
                        .and(cafe.name.contains(keyword))
                        .andAnyOf(cafe.address.contains(region).or(cafe.roadAddress.contains(region))));

        return PageableExecutionUtils.getPage(cafeList, pageable, count::fetchOne);

    }
}
