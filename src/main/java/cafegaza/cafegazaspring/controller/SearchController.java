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
    @GetMapping("/map")
    public String openMapMode(Model model){
        return "map";
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
