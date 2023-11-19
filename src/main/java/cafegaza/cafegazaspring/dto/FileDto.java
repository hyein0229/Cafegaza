package cafegaza.cafegazaspring.dto;

import cafegaza.cafegazaspring.domain.uploadFile.File;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {

    private Long id;
    private String fileStoredName;

    public static FileDto toDto(File file) {
        return FileDto.builder()
                .id(file.getId())
                .fileStoredName(file.getFileStoredName())
                .build();
    }
}
