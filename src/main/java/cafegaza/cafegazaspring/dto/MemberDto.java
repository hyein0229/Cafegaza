package cafegaza.cafegazaspring.dto;

import cafegaza.cafegazaspring.domain.Member;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private Long id;
    private String userId;
    private String password;
    private String nickname;
    private String email;

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

    public static MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .userId(member.getUserId())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .build();
    }
}
