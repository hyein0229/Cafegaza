package cafegaza.cafegazaspring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CafeDto {

    private String name;
    private String roadAddress;
    private String address;
    private String openHours;
    private String telephone;
    private String cafeImageUrl;
    private String detailUrl;
    private double x;
    private double y;

}
