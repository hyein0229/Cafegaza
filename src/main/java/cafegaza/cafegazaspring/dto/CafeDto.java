package cafegaza.cafegazaspring.dto;

import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.domain.Menu;
import cafegaza.cafegazaspring.domain.OpenHour;
import cafegaza.cafegazaspring.domain.Review;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CafeDto {

    private Long id;
    private String name;
    private String roadAddress;
    private String address;
    private String openHours;
    private String telephone;
    private String cafeImageUrl;
    private String detailUrl;
    private double x;
    private double y;
    private int bookmarkCount;
    private double rate;
    private int reviewCount;
    private boolean isOpen;
    private List<MenuDto> menus; // 메뉴

    //-- dto -> entity 변환 메소드 --//
    public Cafe toEntity() {
        return Cafe.builder()
                .cafeId(id)
                .name(name)
                .roadAddress(roadAddress)
                .address(address)
                .openHours(openHours)
                .telephone(telephone)
                .cafeImageUrl(cafeImageUrl)
                .detailUrl(detailUrl)
                .x(x).y(y)
                .bookmarkCount(bookmarkCount)
                .rate(rate)
                .build();
    }

    //-- entity -> dto 변환 메소드 --//
    public static CafeDto toDto(Cafe cafe) {
        return CafeDto.builder()
                .id(cafe.getCafeId())
                .name(cafe.getName())
                .roadAddress(cafe.getRoadAddress())
                .address(cafe.getAddress())
                .openHours(cafe.getOpenHours())
                .telephone(cafe.getTelephone())
                .cafeImageUrl(cafe.getCafeImageUrl())
                .detailUrl(cafe.getDetailUrl())
                .x(cafe.getX()).y(cafe.getY())
                .bookmarkCount(cafe.getBookmarkCount())
                .rate(cafe.getRate())
                .reviewCount(cafe.getReviewCount())
                .menus(cafe.getMenus().stream().map(menu -> MenuDto.toDto(menu)).toList())
                .build();
    }

    // Page<Entity> -> Page<Dto> 변환
    public Page<CafeDto> toDtoList(Page<Cafe> cafes) {
        return cafes.map(cafe -> toDto(cafe));
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }



}
