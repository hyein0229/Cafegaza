package cafegaza.cafegazaspring.dto;

import cafegaza.cafegazaspring.domain.Cafe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCafeResponseDto {

    private int totalPages;
    private long totalElements;
    private List<CafeDto> content;

}
