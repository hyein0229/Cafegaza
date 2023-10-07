package cafegaza.cafegazaspring.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OpenHour extends BaseTime {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    private String day; // 요일

    private int startTime; // 영업 시작 시간

    private int endTime; // 영업 종료 시간

    private int breakStart; // 브레이크 타임 시작

    private int breakEnd; // 브레이크 타임 종료

    //-- 양방향 연관관계 매핑 메소드--//
    public void setCafe(Cafe cafe) {
        this.cafe = cafe;
        cafe.getOpenHourList().add(this);
    }

    public static OpenHour setOpenHour(Cafe cafe, String day, int startTime, int endTime) {
        OpenHour openHour = OpenHour.builder()
                .day(day)
                .startTime(startTime)
                .endTime(endTime)
                .build();
        openHour.setCafe(cafe);
        return openHour;
    }

    public void setBreakStart(int breakStart) {
        this.breakStart = breakStart;
    }
    public void setBreakEnd(int breakEnd) {
        this.breakEnd = breakEnd;
    }



}
