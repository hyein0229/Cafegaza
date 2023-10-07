package cafegaza.cafegazaspring.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Cafe extends BaseTime {

    @Id @GeneratedValue
    private Long cafeId;

    private String name; // 카페명

    private String roadAddress; // 도로명 주소

    private String address; // 지번 주소

    private String openHours; // 영업시간

    private String telephone; // 전화번호

    private String cafeImageUrl; // 대표 이미지 주소

    private String detailUrl; // 카페 상세보기 주소

    private double x; // 경도

    private double y; // 위도

    @ColumnDefault("0") // 0으로 초기화
    private int bookmarkCount; // 즐겨찾기 개수

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>(); // 카페 리뷰 목록

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Menu> menus = new ArrayList<>(); // 메뉴

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL)
    @Builder.Default
    private List<OpenHour> openHourList = new ArrayList<>(); // 영업 시간

}
