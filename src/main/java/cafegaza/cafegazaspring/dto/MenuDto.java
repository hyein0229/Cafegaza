package cafegaza.cafegazaspring.dto;

import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.domain.Menu;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto {

    private Long id;

    private Long cafeId;

    private String menuName; // 메뉴 이름

    private int price; // 가격

    private String menuImage; // 메뉴 이미지 주소

    private int popularity; // 인기 여부

    //-- entity -> dto 변환 메소드 --//
    public static MenuDto toDto(Menu menu) {
        return MenuDto.builder()
                .id(menu.getId())
                .cafeId(menu.getCafe().getCafeId())
                .menuName(menu.getMenuName())
                .price(menu.getPrice())
                .menuImage(menu.getMenuImage())
                .popularity(menu.getPopularity())
                .build();
    }

}
