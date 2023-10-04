package cafegaza.cafegazaspring.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Menu extends BaseTime{

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe; // 카페

    private String menuName; // 메뉴 이름

    private int price; // 가격

    private String menuImage; // 메뉴 이미지 주소

    private int popularity; // 인기 여부

    //--양방향 연관관계 매핑 메소드--//
    public void setCafe(Cafe cafe){
        this.cafe = cafe;
        cafe.getMenus().add(this); // 카페에도 메뉴 추가
    }

    public static Menu createMenu(Cafe cafe, String menuName, int price, String menuImage, int popularity) {
        Menu menu = Menu.builder()
                .menuName(menuName)
                .price(price)
                .menuImage(menuImage)
                .popularity(popularity).build();
        menu.setCafe(cafe);
        return menu;
    }



}
