package cafegaza.cafegazaspring.service;

import cafegaza.cafegazaspring.domain.Member;
import cafegaza.cafegazaspring.dto.MemberDto;
import cafegaza.cafegazaspring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Pattern;

/*
    로그인
 */
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public boolean login(String userId, String password) {
        Optional<Member> findMember = memberRepository.findByUserId(userId);

        checkId(findMember);
        checkPassword(findMember, password);

        return true;
    }

    // 사용자가 입력한 아이디가 DB에 저장되어 있는 아이디인지 확인
    public void checkId(Optional<Member> findMember) {
        findMember.orElseThrow(() -> new NoSuchElementException("아이디를 찾을 수 없습니다."));
    }

    // 아이디가 저장되어 있다면,
    // 사용자가 입력한 비밀번호가 해당 아이디의 비밀번호와 일치하는 지 확인
    public void checkPassword(Optional<Member> findMember, String password) {

        if (!findMember.get().getPassword().equals(password)) {
            throw new IllegalStateException("비밀번호가 틀렸습니다.");
        }
    }

    public Optional<Member> returnMemberData(String userId) {
        Optional<Member> memberData = memberRepository.findByUserId(userId);

        return memberData;
    }
}
