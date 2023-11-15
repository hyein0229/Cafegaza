package cafegaza.cafegazaspring.dto;

import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.domain.OpenHour;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

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
    private List<MenuDto> menus; // 메뉴
    private boolean isOpen;
    private boolean isBookmark; // 북마크 여부

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
                .isOpen(checkOpen(cafe))
                .build();
    }

    // Page<Entity> -> Page<Dto> 변환
    public Page<CafeDto> toDtoList(Page<Cafe> cafes) {
        return cafes.map(cafe -> toDto(cafe));
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
    public void setIsBookmark (boolean isBookmark){
        this.isBookmark = isBookmark;
    }

    public static boolean checkOpen(Cafe cafe) {
        LocalTime now = LocalTime.now();
        int currentTime = now.getHour() * 60 + now.getMinute();
        String today = getToday();
        Optional<OpenHour> todayOpenHour = cafe.getOpenHourList().stream().filter(openHour -> openHour.getDay().equals(today)).findFirst();
        if (!todayOpenHour.isEmpty()) {
            int startTime = todayOpenHour.get().getStartTime();
            int endTime = todayOpenHour.get().getEndTime();
            if (startTime < currentTime && endTime > currentTime) {
                return true;
            }
            return false;
        }
        return false;
    }

    private static String getToday() {
        List<String> list = Arrays.asList("일", "월", "화", "수", "목", "금", "토", "일");
        Calendar cal = Calendar.getInstance();
        String dayOfWeek = list.get(cal.get(Calendar.DAY_OF_WEEK) - 1); // 현재 요일 구하기, 1~7 -> 일, 월~금, 토
        return dayOfWeek;
    }



}
