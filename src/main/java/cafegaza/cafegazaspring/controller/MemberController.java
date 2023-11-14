package cafegaza.cafegazaspring.controller;

import cafegaza.cafegazaspring.domain.Member;
import cafegaza.cafegazaspring.dto.MemberProfileDto;
import cafegaza.cafegazaspring.security.CustomMemberDetails;
import cafegaza.cafegazaspring.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/join")
    public ResponseEntity joinProcess(@RequestParam("id") String userId, @RequestParam("password") String password,
                                      @RequestParam("nickname") String nickname, @RequestParam("email") String email) {

        boolean checkUserId = memberService.alreadyExistUserId(userId);
        boolean checkNickname = memberService.alreadyExistNickname(nickname);
        boolean checkPwd = memberService.validPassword(password);

        // 아이디 중복 확인
        if(!checkUserId) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"중복되는 아이디입니다\"}");
        }

        // 닉네임 중복 확인
        if(!checkNickname) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"중복되는 닉네임입니다\"}");
        }

        // 비밀번호 조건 충족 확인
        if(!checkPwd) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"영어 대소문자, 숫자, 특수문자를 포함한 8~16자리의 비밀번호를 생성해주세요\"}");
        }

        // 아이디와 닉네임 중복 X, 비밀번호 조건 충족, 메일 인증 완료 시, 회원가입 실시
        if(checkUserId && checkNickname && checkPwd && checkAuthMail) {
            memberService.join(userId, nickname, password, email);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"회원가입 성공 ;)\"}");
        }

        throw new IllegalStateException("회원 가입 오류");
    }


    // 인증 메일 전송
    @PostMapping("/mailSend")
    public ResponseEntity sendMail(@RequestParam("email") String email) throws Exception {
        if (memberService.sendAuthMail(email)) {
            return ResponseEntity.ok().body("인증 번호 전송 완료");
        }

        throw new IllegalStateException("인증 번호 전송 오류");
    }

    // 인증 번호 일치/불일치 확인
    @PostMapping("/mailAuth")
    public ResponseEntity CheckMailAuth(@RequestParam("emailAuthText") String emailAuth) {
        if (memberService.checkAuthCode(emailAuth)) {
            checkAuthMail = true;
            return ResponseEntity.ok().body("메일 인증 완료");
        }

        throw new IllegalStateException("인증 번호 불일치");
    }


    /*
        로그인
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity loginProcess(@RequestParam("memberId") String memberId, @RequestParam("memberPassword") String password,
                                       Model model, HttpServletRequest httpServletRequest) {

        // 로그인 성공 시
        if(memberService.login(memberId, password)) {

            Optional<Member> memberData = memberService.returnMemberData(memberId);

            // 기존 세션 파기
            httpServletRequest.getSession().invalidate();
            // 세션이 생성되어 있다면 세션 가져오고, 없다면 세션 생성
            HttpSession session = httpServletRequest.getSession(true);
            session.setAttribute("sessionId", memberData.get().getId());
            // 세션 30분 동안 유지
            session.setMaxInactiveInterval(1800);

            return ResponseEntity.ok("login success");
        }

        // 로그인 실패 시
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @GetMapping("/myPage")
    public String myProfile(Model model, @AuthenticationPrincipal CustomMemberDetails memberDetails) {
        MemberProfileDto myProfileDto = memberService.myProfileDto(memberDetails.getId());
        model.addAttribute("myProfileDto", myProfileDto);

        return "myPage";
    }

//    @GetMapping("/memberPage/{pageUserId}")
//    public String memberProfile(@PathVariable long pageUserId, Model model, @AuthenticationPrincipal MemberImpl member) {
//        MemberProfileDto memberProfileDto = memberService.memberProfileDto(pageUserId, member.getId());
//        model.addAttribute("memberDto", memberProfileDto);
//
//        return "memberPage";
//    }
}
