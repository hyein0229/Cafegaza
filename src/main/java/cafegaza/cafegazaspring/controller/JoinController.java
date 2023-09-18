package cafegaza.cafegazaspring.controller;

import cafegaza.cafegazaspring.dto.MemberDto;
import cafegaza.cafegazaspring.service.JoinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;
    private boolean checkAuthMail = false;    // 메일 인증 성공 여부

    @GetMapping("/join")
    public String join() {
        return "join";
    }

    /*
        추후, ui가 완성됨에 따라 View에 보여 줄 정보 처리 필요
     */
    @PostMapping("/join")
    public String joinProcess(@Valid @ModelAttribute MemberDto memberDto ,BindingResult bindingResult) {

        // 회원가입 성공 시, 홈 화면으로 이동
        if(joinService.join(memberDto) & checkAuthMail & !bindingResult.hasErrors()) {
            return "redirect:/";
        }

        // 회원가입 실패 시, 회원가입 페이지 유지
        return "join";
    }


    // 인증 메일 전송
    @PostMapping("/mailSend")
    public String sendMail(@RequestParam String emailSend) throws Exception {
        joinService.sendAuthMail(emailSend);

        return "join";
    }

    // 인증 번호 일치/불일치 확인
    @PostMapping("/mailAuth")
    public String CheckMailAuth(String emailAuth) {
        checkAuthMail =  joinService.checkAuthCode(emailAuth);

        return "join";
    }
}
