package cafegaza.cafegazaspring.service;

import cafegaza.cafegazaspring.dto.SearchQuery;
import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.domain.Menu;
import cafegaza.cafegazaspring.domain.OpenHour;
import cafegaza.cafegazaspring.dto.CafeDto;
import cafegaza.cafegazaspring.dto.KakaoSearchApiResDto;
import cafegaza.cafegazaspring.repository.CafeRepository;
import cafegaza.cafegazaspring.repository.MenuRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class SearchServiceTest {

    @Autowired
    SearchService searchService;
    @Autowired CafeRepository cafeRepository;
    @Autowired MenuRepository menuRepository;
    @Autowired KakaoOpenApiManager kakaoOpenApiManager;

    // 카카오 키워드로 장소 검색하기 rest api 호출 테스트
    @Test
    public void kakaoApiTest() throws Exception {
        KakaoSearchApiResDto searchKeywordlResDto = kakaoOpenApiManager.kakaomapSearchApi("합정역 카페");
        System.out.println(searchKeywordlResDto.getMeta().getSame_name().getKeyword());
        System.out.println(searchKeywordlResDto.getMeta().getSame_name().getSelected_region());
    }

    /*
        페이징을 사용한 키워드 카페 검색 테스트
     */
    @Test
    public void findByRegionTest() throws Exception {
        Pageable pageable = PageRequest.of(0, 15);
        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setKeyword("강남구 카페"); // 키워드 쿼리 지정
        searchQuery.setMenuOption("");
        searchQuery.setMaxPrice(0);

        Page<CafeDto> searchCafe = searchService.searchCafe(searchQuery, null, pageable);

//        System.out.println(searchCafe.getTotalElements());
//        System.out.println(searchCafe.getContent().size());
        Assertions.assertEquals(500, searchCafe.getTotalElements()); // 강남구의 500개의 카페가 모두 검색되었는 지 확인
        Assertions.assertEquals(15, searchCafe.getContent().size()); // 15개의 목록만 가져왔는지 확인

    }

    /*
        지역 + 메뉴 옵션 검색
     */
    @Test
    public void findByRegionAndMenuTest() throws Exception {
        Pageable pageable = PageRequest.of(1, 15);
        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setKeyword("서울 카페"); // 키워드 쿼리 지정
        searchQuery.setMenuOption("아메리카노");
        searchQuery.setMaxPrice(0);

        Page<Cafe> searchCafe = searchService.searchCafe(searchQuery,null, pageable).map(cafeDto -> cafeDto.toEntity());

        for(Cafe cafe : searchCafe) {
            List<Menu> menus = menuRepository.findByCafe(cafe);
            Assertions.assertEquals(true, !menus.stream().filter(menu -> menu.getMenuName().contains("아메리카노")).toList().isEmpty());
        }
        System.out.println(searchCafe.getTotalElements());
    }

    /*
        지역명 + 키워드 검색
     */
    @Test
    public void findByRegionAndKeywordTest() throws Exception {
        Pageable pageable = PageRequest.of(1, 15);
        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setKeyword("강남 스타벅스"); // 키워드 쿼리 지정
        searchQuery.setMenuOption("");
        searchQuery.setMaxPrice(0);

        Page<CafeDto> searchCafe = searchService.searchCafe(searchQuery, null, pageable);

        Assertions.assertEquals(78, searchCafe.getTotalElements());
        Assertions.assertEquals(15, searchCafe.getSize());

    }

    /*
        반경 내 카페 찾기 querydsl 테스트
     */
    @Test
    public void findByDistanceQuerydslTest() {
        Pageable pageable = PageRequest.of(0, 15);
        double[] centerCoord = {126.901347294861, 37.5567856576915};

        Page<Cafe> queryDslResult = cafeRepository.findByMultipleCond(new SearchQuery(), pageable);

        for(int i=0; i<queryDslResult.getSize(); i++) {
            System.out.println(queryDslResult.getContent().get(i).getRoadAddress());
        }
        System.out.println(queryDslResult.getTotalElements());
    }

    /*
        반경 + 키워드 + 메뉴 검색
     */
    @Test
    public void findByDistanceKeywordMenuTest() throws Exception {
        Pageable pageable = PageRequest.of(0, 15);
        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setKeyword("합정역 스타벅스"); // 키워드 쿼리 지정
        searchQuery.setMenuOption("아메리카노");

        Page<Cafe> searchCafe = searchService.searchCafe(searchQuery, null, pageable).map(cafeDto -> cafeDto.toEntity());

        for(Cafe cafe : searchCafe) {
            List<Menu> menus = menuRepository.findByCafe(cafe);
            Assertions.assertEquals(true, !menus.stream().filter(menu -> menu.getMenuName().contains("아메리카노")).toList().isEmpty());
            Assertions.assertEquals(true, cafe.getName().contains("스타벅스"));
        }
        System.out.println("totalElements: " + searchCafe.getTotalElements());

    }

    /*
        지역명 + 메뉴 + 가격 옵션
     */
    @Test
    public void fineByPriceOption() throws Exception {
        Pageable pageable = PageRequest.of(1, 15);
        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setKeyword("서울 카페"); // 키워드 쿼리 지정
        searchQuery.setMenuOption("아메리카노");
        searchQuery.setMaxPrice(7000); // maxPrice -> 7000원으로 설정

        Page<Cafe> searchCafe = searchService.searchCafe(searchQuery, null, pageable).map(cafeDto -> cafeDto.toEntity());

        for(Cafe cafe : searchCafe) {
            List<Menu> menus = menuRepository.findByCafe(cafe);
            Assertions.assertEquals(true, !menus.stream().filter(menu -> menu.getMenuName().contains("아메리카노")).toList().isEmpty());
        }

        Assertions.assertEquals(5493, searchCafe.getTotalElements()); // 직접 쿼리했을 때 나온 결과와 같은지 확인
    }

    @Test
    public void findByOpenHour() throws Exception {

        Pageable pageable = PageRequest.of(0, 15);
        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setKeyword("서울 카페"); // 키워드 쿼리 지정
        searchQuery.setMenuOption("");
        searchQuery.setMaxPrice(0);
        searchQuery.setStartHour(780);
        searchQuery.setEndHour(840);

        Page<Cafe> searchCafe = searchService.searchCafe(searchQuery, null, pageable).map(cafeDto -> cafeDto.toEntity());

        for(Cafe cafe : searchCafe) {
            List<OpenHour> openHourList = cafe.getOpenHourList();
            for(OpenHour openHour : openHourList) {
                Assertions.assertEquals(true, openHour.getStartTime() <= 780 && openHour.getEndTime() >= 840);
            }
        }
        System.out.println(searchCafe.getTotalElements());
    }

}
