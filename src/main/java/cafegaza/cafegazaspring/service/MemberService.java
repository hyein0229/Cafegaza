package cafegaza.cafegazaspring.service;

import cafegaza.cafegazaspring.domain.Member;
import cafegaza.cafegazaspring.dto.MemberDto;
import cafegaza.cafegazaspring.dto.MemberProfileDto;
import cafegaza.cafegazaspring.repository.FollowRepository;
import cafegaza.cafegazaspring.repository.MemberRepository;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final JavaMailSender javaMailSender;
    private final String emailAuthCode = createCode();
    private boolean checkEmailAuth = false;

    /*
        회원가입
     */
    public boolean join(String userId, String nickname, String password) {

        if (checkEmailAuth){
            Member member = new Member();
            member.setUserId(userId);
            member.setNickname(nickname);
            member.setPassword(password);

            memberRepository.save(member);
            return true;
        }

        return false;
    }

    // userId 중복 확인
    public boolean alreadyExistUserId(String userId) {
        memberRepository.findByUserId(userId).ifPresent(u -> {
            throw new IllegalStateException("아이디 중복");
        });

        return true;
    }

    // nickname 중복 확인
    public boolean alreadyExistNickname(String nickname) {
        memberRepository.findByNickname(nickname).ifPresent(n -> {
            throw new IllegalStateException("닉네임 중복");
        });

        return true;
    }

    // password 조건 충족 여부 확인
    // 영어 대소문자, 숫자, 특수문자를 포함한 8~16자리
    public boolean validPassword(String pwd) {
        String passwordPattern = "^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{8,16}+$";
        boolean validPattern = Pattern.matches(passwordPattern, pwd);

        if (!validPattern) {
            throw new IllegalStateException("잘못된 비밀번호 형식");
        }

        return true;
    }

    // 인증 번호 8자리 생성
    private static String createCode() {
        StringBuffer code = new StringBuffer();
        Random random = new Random();

        for (int i=0; i<8; i++) {

            // 0~2 중 랜덤 선택
            int index = random.nextInt(3);

            switch (index) {
                //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                case 0:
                    code.append((char) ((int) (random.nextInt(26)) + 97));
                    break;

                //  A~Z
                case 1:
                    code.append((char) ((int) (random.nextInt(26)) + 65));
                    break;

                // 0~9
                case 2:
                    code.append((random.nextInt(10)));
                    break;
            }
        }

        return code.toString();
    }

    // 인증 번호 메일 양식
    private MimeMessage createMessage(String to) throws Exception {

        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, to);    // 인증 메일 받을 대상
        message.setSubject("[카페가자] 회원가입 이메일 인증 번호");      // 인증 메일 제목
        String msg = "";    // 인증 메일 내용
        msg+= "<div style='margin:100px;'>";
        msg+= "<h1> [카페가자] 인증 번호</h1>";
        msg+= "<br>";
        msg+= "<p>아래의 인증 코드를 입력해주세요.<p>";
        msg+= "<p>감사합니다 ;)<p>";
        msg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msg+= "<h3 style='color:blue;'> 이메일 인증 번호</h3>";
        msg+= "<div style='font-size:130%'>";
        msg+= "<strong>"+ emailAuthCode +"</strong><div><br/> ";
        msg+= "</div>";

        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress("gangjiwon70@gmail.com","카페가자"));

        return message;
    }

    // 인증 메일 보내기
    public void sendAuthMail(String to) throws Exception {
        MimeMessage message = createMessage(to);

        try{
            javaMailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException("잘못된 메일 형식 입력");
        }
    }

    // 인증 번호 일치/불일치 확인
    public boolean checkAuthCode(String emailAuthCode) {
        if (!emailAuthCode.equals(this.emailAuthCode)) {
            throw new IllegalStateException("잘못된 인증번호 입력");
        }

        checkEmailAuth = true;
        return true;
    }

    /*
        로그인
     */
    public boolean login(String userId, String password) {
        Optional<Member> findMember = memberRepository.findByUserId(userId);

        if (checkId(findMember) && checkPassword(findMember, password)){ return true; }

        return false;
    }

    // 사용자가 입력한 아이디가 DB에 저장되어 있는 아이디인지 확인
    public boolean checkId(Optional<Member> findMember) {
        if (findMember != null) { return true; }

        return false;
    }

    // 아이디가 저장되어 있다면,
    // 사용자가 입력한 비밀번호가 해당 아이디의 비밀번호와 일치하는 지 확인
    public boolean checkPassword(Optional<Member> findMember, String password) {
        if (findMember.get().getPassword().equals(password)) {
            return true;
        }

        return false;
    }

    public Optional<Member> returnMemberData(String userId) {
        Optional<Member> memberData = memberRepository.findByUserId(userId);

        return memberData;
    }

    /*
        myPage에 follow 정보 출력
     */
    @Transactional(readOnly = true)
    public MemberProfileDto myProfileDto(Long sessionId) {
        MemberProfileDto myProfileDto = new MemberProfileDto();

        Member member = memberRepository.findById(sessionId).orElseThrow();

        int followingCount = followRepository.followingCount(sessionId);
        int followedCount = followRepository.followingCount(sessionId);

        myProfileDto.setMember(member);
        myProfileDto.setFollowingCount(followingCount);
        myProfileDto.setFollowedCount(followedCount);

        return myProfileDto;
    }

    /*
        memberPage에 follow 정보 출력
        pageUserId: 해당 memberPage의 member ID
        sessionId: 로그인되어 있는 member ID
     */
    @Transactional(readOnly = true)
    public MemberProfileDto memberProfileDto(Long pageUserId, Long sessionId) {
        MemberProfileDto memberProfileDto = new MemberProfileDto();

        Member member = memberRepository.findById(pageUserId).orElseThrow();

        int followState = followRepository.followState(sessionId, pageUserId);
        int followingCount = followRepository.followingCount(pageUserId);
        int followedCount = followRepository.followingCount(pageUserId);

        memberProfileDto.setMember(member);
        memberProfileDto.setFollowState(followState == 1);
        memberProfileDto.setFollowingCount(followingCount);
        memberProfileDto.setFollowedCount(followedCount);

        return memberProfileDto;
    }
}
