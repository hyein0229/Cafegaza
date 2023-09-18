package cafegaza.cafegazaspring.service;

import cafegaza.cafegazaspring.controller.SearchQuery;
import cafegaza.cafegazaspring.dto.KakaoSearchApiResDto;
import cafegaza.cafegazaspring.dto.SearchCafeResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
public class CafeSearchServiceTest {

    @Autowired CafeSearchService cafeSearchService;
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
    public void searchCafeTest() throws Exception {
        Pageable pageable = PageRequest.of(0, 15);
        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setKeyword("강남구 카페"); // 키워드 쿼리 지정
        SearchCafeResponseDto searchCafeResponseDto = cafeSearchService.searchCafe(searchQuery, pageable);

        Assertions.assertEquals(500, searchCafeResponseDto.getTotalElements()); // 강남구의 500개의 카페가 모두 검색되었는 지 확인
        Assertions.assertEquals(15, searchCafeResponseDto.getContent().size()); // 15개의 목록만 가져왔는지 확인

    }

    @Test
    public void regexTest() {
        String keyword = "합정동";
        if (keyword.matches(".+[읍|면|동]")) {
            System.out.println("matched");
        } else {
            System.out.println("not matched");
        }
    }

}
