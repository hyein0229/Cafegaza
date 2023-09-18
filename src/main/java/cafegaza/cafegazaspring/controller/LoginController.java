package cafegaza.cafegazaspring.controller;

import cafegaza.cafegazaspring.domain.Member;
import cafegaza.cafegazaspring.dto.MemberDto;
import cafegaza.cafegazaspring.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /*
        추후, ui가 완성됨에 따라 View에 보여 줄 정보 처리 필요
     */
    @PostMapping("/login")
    public String loginProcess(String userId, String password, HttpServletRequest httpServletRequest) {

        System.out.println(userId);
        System.out.println(password);

        // 로그인 성공 시, 홈 화면으로 이동
        if(loginService.login(userId, password)) {

            Optional<Member> memberData = loginService.returnMemberData(userId);

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
