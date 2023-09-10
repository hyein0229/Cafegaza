package cafegaza.cafegazaspring.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

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



}
