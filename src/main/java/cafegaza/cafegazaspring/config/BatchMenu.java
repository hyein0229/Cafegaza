package cafegaza.cafegazaspring.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchMenu {

    private String cafeName;
    private String detailUrl;
    private String menuStrDic;
}
