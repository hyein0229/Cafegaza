package cafegaza.cafegazaspring.domain.uploadFile;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

// 상속관계 매핑
//@Entity
//@Getter
//@SuperBuilder
//@NoArgsConstructor
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass // 자식 클래스에서 컬럼 상속
@Getter
public abstract class UploadFile {

    @Id @GeneratedValue
    private Long id;

    private String fileOrigName; // 원래 이름

    private String fileStoredName; // 저장된 이름

    private String filePath; // 저장 경로

}
