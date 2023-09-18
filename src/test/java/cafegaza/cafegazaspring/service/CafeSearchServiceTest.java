package cafegaza.cafegazaspring.service;

import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.dto.SearchKeywordlResDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CafeSearchServiceTest {

    @Autowired CafeSearchService cafeSearchService;
    @Autowired KakaoOpenApiManager kakaoOpenApiManager;

    // 카카오 키워드로 장소 검색하기 rest api 호출 테스트
    @Test
    public void kakaoApiTest() throws Exception {
        SearchKeywordlResDto searchKeywordlResDto = kakaoOpenApiManager.kakaomapSearchApi("합정역 카페");
        System.out.println(searchKeywordlResDto.getMeta().getSame_name().getKeyword());
        System.out.println(searchKeywordlResDto.getMeta().getSame_name().getSelected_region());
    }

    @Test
    public void searchCafeTest() throws Exception {
        List<Cafe> searchResult = cafeSearchService.searchCafe("서초 이디야");
        if (searchResult.isEmpty()) {
            System.out.println("empty result");
        } else{
            System.out.println(searchResult.size());
            searchResult.forEach(cafe -> {
                System.out.println(cafe.getName() + " " + cafe.getRoadAddress());
            });
        }

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
