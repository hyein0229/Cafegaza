package cafegaza.cafegazaspring.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseTime{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;     // 시스템 상 고유 번호

    @NotBlank
    private String userId;

    @NotBlank
    private String password;

    @NotBlank
    private String nickname;

    @Email
    private String email;
}
