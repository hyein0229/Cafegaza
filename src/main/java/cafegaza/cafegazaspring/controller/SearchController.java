package cafegaza.cafegazaspring.controller;

import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.service.CafeSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final CafeSearchService cafeSearchService;

    /*
        지도모드 클릭 시 지도 뷰 열기
     */
    @GetMapping("/map")
    public String openMapMode(Model model){

        return "map";
    }

    /*
        검색 요청이 오면 질의어에 맞는 카페 리스트를 찾아 데이터 반환 (rest api)
     */
    @ResponseBody // rest api
    @PostMapping("/map/search")
    public ResponseEntity<List<Cafe>> searchCafe(@RequestBody SearchQuery searchQuery) throws Exception{
        List<Cafe> searchResult = cafeSearchService.searchCafe(searchQuery.getKeyword()); // 현재는 키워드로만 검색 가능
        return new ResponseEntity<>(searchResult, HttpStatus.OK);
    }
}
