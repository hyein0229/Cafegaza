package cafegaza.cafegazaspring.service;

import cafegaza.cafegazaspring.controller.SearchQuery;
import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.domain.Menu;
import cafegaza.cafegazaspring.dto.CafeDto;
import cafegaza.cafegazaspring.dto.KakaoSearchApiResDto;
import cafegaza.cafegazaspring.repository.CafeRepository;
import cafegaza.cafegazaspring.repository.MenuRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CafeSearchServiceTest {

    @Autowired CafeSearchService cafeSearchService;
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

        Page<CafeDto> searchCafe = cafeSearchService.searchCafe(searchQuery, pageable);

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
        searchQuery.setKeyword("마포 카페"); // 키워드 쿼리 지정
        searchQuery.setMenuOption("아메리카노");

        Page<Cafe> searchCafe = cafeSearchService.searchCafe(searchQuery, pageable).map(cafeDto -> cafeDto.toEntity());

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

        Page<CafeDto> searchCafe = cafeSearchService.searchCafe(searchQuery, pageable);

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

        Page<Cafe> queryDslResult = cafeRepository.findByMultipleCond(null, "", centerCoord, new SearchQuery(), pageable);

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

        Page<Cafe> searchCafe = cafeSearchService.searchCafe(searchQuery, pageable).map(cafeDto -> cafeDto.toEntity());

        for(Cafe cafe : searchCafe) {
            List<Menu> menus = menuRepository.findByCafe(cafe);
            Assertions.assertEquals(true, !menus.stream().filter(menu -> menu.getMenuName().contains("아메리카노")).toList().isEmpty());
            Assertions.assertEquals(true, cafe.getName().contains("스타벅스"));
        }
        System.out.println("totalElements: " + searchCafe.getTotalElements());

    }

}
