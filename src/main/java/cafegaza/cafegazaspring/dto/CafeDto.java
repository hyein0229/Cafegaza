package cafegaza.cafegazaspring.dto;

import cafegaza.cafegazaspring.domain.Cafe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

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
                .build();
    }

    // Page<Entity> -> Page<Dto> 변환
    public Page<CafeDto> toDtoList(Page<Cafe> cafes) {
        return cafes.map(cafe -> toDto(cafe));
    }



}
