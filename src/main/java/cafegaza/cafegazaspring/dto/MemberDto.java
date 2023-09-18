package cafegaza.cafegazaspring.dto;

import cafegaza.cafegazaspring.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberDto {

    private long id;
    private String userId;
    private String password;
    private String nickname;
    private String email;

    @Builder
    public MemberDto(long id, String userId, String password, String nickname, String email) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
    }

    public Member toEntity() {
        Member member = Member.builder()
                .id(id)
                .userId(userId)
                .password(password)
                .nickname(nickname)
                .email(email)
                .build();

        return member;
    }

}
