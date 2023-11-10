package cafegaza.cafegazaspring.controller;

import cafegaza.cafegazaspring.dto.CafeDto;
import cafegaza.cafegazaspring.service.CafeSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CafeSearchService cafeSearchService;

    @GetMapping("/")
    public String home(@SessionAttribute(name = "sessionId", required = false) Long memberId, Model model) {
//        if(memberId == null) {
//            return "home";
//        }
        List<CafeDto> popularPlaces = cafeSearchService.getPopularPlaces();
        model.addAttribute("member", memberId);
        model.addAttribute("popularPlaces", popularPlaces);
        return "home";
    }

}
