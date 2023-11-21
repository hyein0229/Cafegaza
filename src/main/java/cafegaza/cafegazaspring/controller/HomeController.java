package cafegaza.cafegazaspring.controller;

import cafegaza.cafegazaspring.dto.CafeDto;
import cafegaza.cafegazaspring.service.CafeService;
import cafegaza.cafegazaspring.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CafeService cafeService;

    @GetMapping("/")
    public String home(@SessionAttribute(name = "sessionId", required = false) Long memberId, Model model) {

        if(memberId != null) {
            model.addAttribute("member", memberId);
        }

        List<CafeDto> popularPlaces = cafeService.getPopularPlaces();
        model.addAttribute("popularPlaces", popularPlaces);
        return "home";
    }

}
