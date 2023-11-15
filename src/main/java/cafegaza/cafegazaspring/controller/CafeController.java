package cafegaza.cafegazaspring.controller;

import cafegaza.cafegazaspring.dto.CafeDto;
import cafegaza.cafegazaspring.dto.ReviewForm;
import cafegaza.cafegazaspring.service.CafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class CafeController {

    private final CafeService cafeService;

    /**
     * 가게 디테일 페이지
     */
    @GetMapping("/cafe/view/{cafeId}")
    public String viewDetail (@SessionAttribute(name = "sessionId", required = false) Long memberId, @PathVariable("cafeId")Long cafeId, Model model){
        if(memberId != null) {
            model.addAttribute("member", memberId);
        }
        CafeDto cafeDto = cafeService.findById(cafeId, memberId);
        model.addAttribute("cafe", cafeDto);
        model.addAttribute("reviewForm", new ReviewForm());
        return "view";
    }

    /**
     가게 사진 가져오기
     */
    @ResponseBody
    @GetMapping("/image/{cafeId}") // ?page=0&size=15
    public Page<String> getReviewImages(@PathVariable("cafeId") Long cafeId, Pageable pageable) {
        return cafeService.getImages(cafeId, pageable);
    }

}
