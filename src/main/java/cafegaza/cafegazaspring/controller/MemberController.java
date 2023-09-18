package cafegaza.cafegazaspring.controller;

import cafegaza.cafegazaspring.domain.Member;
import cafegaza.cafegazaspring.dto.MemberDto;
import cafegaza.cafegazaspring.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private boolean checkAuthMail = false;    // 메일 인증 성공 여부

    /*
        회원가입
     */
    @GetMapping("/join")
    public String join() {
        return "join";
    }

    // 추후, ui가 완성됨에 따라 View에 보여 줄 정보 처리 필요
    @PostMapping("/join")
    public String joinProcess(@Valid @ModelAttribute MemberDto memberDto , BindingResult bindingResult) {

        // 회원가입 성공 시, 홈 화면으로 이동
        if(memberService.join(memberDto) && checkAuthMail && bindingResult.hasErrors()) {

            return "redirect:/";
        }

        return  "join";
    }


    // 인증 메일 전송
    @PostMapping("/mailSend")
    public String sendMail(@RequestParam String emailSend) throws Exception {
        memberService.sendAuthMail(emailSend);

        return "join";
    }

    // 인증 번호 일치/불일치 확인
    @PostMapping("/mailAuth")
    public String CheckMailAuth(String emailAuth) {
        checkAuthMail =  memberService.checkAuthCode(emailAuth);

        return "join";
    }


    /*
        로그인
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // 추후, ui가 완성됨에 따라 View에 보여 줄 정보 처리 필요
    @PostMapping("/login")
    public String loginProcess(String userId, String password, HttpServletRequest httpServletRequest) {

        // 로그인 성공 시, 홈 화면으로 이동
        if(memberService.login(userId, password)) {

            Optional<Member> memberData = memberService.returnMemberData(userId);

            // 기존 세션 파기
            httpServletRequest.getSession().invalidate();
            // 세션이 생성되어 있다면 세션 가져오고, 없다면 세션 생성
            HttpSession session = httpServletRequest.getSession(true);
            session.setAttribute("sessionId", memberData.get().getId());
            // 세션 30분 동안 유지
            session.setMaxInactiveInterval(1800);

            return "redirect:/";
        }

        // 로그인 실패 시, 로그인 페이지 유지
        return "login";
    }
}
