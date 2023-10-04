package cafegaza.cafegazaspring.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark extends BaseTime{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OnDelete(action = OnDeleteAction.CASCADE)  // cafe 삭제 시, cafe와 관련된 bookmark 데이터 같이 삭제
    @ManyToOne(fetch = FetchType.LAZY)
    private Cafe cafe;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

}
