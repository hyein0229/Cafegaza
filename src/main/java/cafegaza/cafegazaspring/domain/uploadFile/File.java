package cafegaza.cafegazaspring.domain.uploadFile;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

// 상속관계 매핑
@SuperBuilder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@NoArgsConstructor
@Getter
@Entity
public abstract class File {

    @Id @GeneratedValue
    private Long id;

    private String fileOrigName; // 원래 이름

    private String fileStoredName; // 저장된 이름

    private String filePath; // 저장 경로

}
