package cafegaza.cafegazaspring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@SessionAttribute(name = "sessionId", required = false) Long memberId, Model model) {
        if(memberId == null) {
            return "home";
        }
        model.addAttribute("member", memberId);
        return "home";
    }
}
