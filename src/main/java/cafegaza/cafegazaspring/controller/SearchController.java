package cafegaza.cafegazaspring.controller;

import cafegaza.cafegazaspring.dto.CafeDto;
import cafegaza.cafegazaspring.service.CafeSearchService;
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

    private final CafeSearchService cafeSearchService;

    /**
        지도모드 클릭 시 지도 뷰 열기
     */
    @GetMapping("/cafe/map")
    public String openMapMode(Model model){
        return "searchMap";
    }

    @GetMapping("/cafe/main")
    public String openSearchMain(@SessionAttribute(name = "sessionId", required = false) Long memberId, Model model) {
        // 로그인이 되어있다면
        if (memberId != null) {
            model.addAttribute("member", memberId);
        }
        return "searchMain";
    }

    /**
     * 가게 디테일 페이지
     */
    @GetMapping("/cafe/view/{cafeId}")
    public String viewDetail (@SessionAttribute(name = "sessionId", required = false) Long memberId, @PathVariable("cafeId")Long cafeId,  Model model){
        if(memberId != null) {
            model.addAttribute("member", memberId);
        }
        CafeDto cafeDto = cafeSearchService.findById(cafeId);
        model.addAttribute("cafe", cafeDto);
        model.addAttribute("reviewForm", new ReviewForm());
        return "view";
    }

    /**
        검색 요청이 오면 질의어에 맞는 카페 리스트를 찾아 데이터 반환 (rest api)
     */
    @ResponseBody // rest api
    @PostMapping("/search")
    public ResponseEntity<Page<CafeDto>> searchCafe(@RequestBody SearchQuery searchQuery, Pageable pageable) throws Exception{
        Page<CafeDto> searchCafe = cafeSearchService.searchCafe(searchQuery, pageable); // 검색 응답 dto 반환
        return new ResponseEntity<>(searchCafe, HttpStatus.OK);
    }
}
