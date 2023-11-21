package cafegaza.cafegazaspring.controller;

import cafegaza.cafegazaspring.dto.CafeDto;
import cafegaza.cafegazaspring.dto.SearchQuery;
import cafegaza.cafegazaspring.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    /**
     * 카페 찾기 메인 페이지
     */
    @GetMapping("/cafe/main")
    public String openSearchMain(@SessionAttribute(name = "sessionId", required = false) Long memberId, Model model) {
        // 로그인이 되어있다면
        if (memberId != null) {
            model.addAttribute("member", memberId);
        }
        return "searchMain";
    }

    /**
        지도모드 열기
     */
    @GetMapping("/cafe/map")
    public String openMapMode(@SessionAttribute(name = "sessionId", required = false) Long memberId, Model model){
        if (memberId != null) {
            model.addAttribute("member", memberId);
        }

        return "searchMap";
    }

    /**
        검색 요청이 오면 질의어에 맞는 카페 리스트를 찾아 데이터 반환 (rest api)
     */
    @ResponseBody
    @PostMapping("/search")
    public ResponseEntity<Page<CafeDto>> searchCafe(@SessionAttribute(name = "sessionId", required = false) Long memberId, @RequestBody SearchQuery searchQuery, Pageable pageable) throws Exception{
        Page<CafeDto> searchCafe = searchService.searchCafe(searchQuery, memberId, pageable); // 검색 응답 dto 반환
        return new ResponseEntity<>(searchCafe, HttpStatus.OK);
    }
}
