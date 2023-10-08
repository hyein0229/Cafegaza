package cafegaza.cafegazaspring.controller;

import lombok.Getter;
import lombok.Setter;

// 검색 시 ajax 쿼리를 받을 dto class
@Getter
@Setter
public class SearchQuery {
    private String keyword; // 검색 키워드
    private String menuOption; // 메뉴 이름 검색
    private int maxPrice = 0; // 가격 옵션
    private int startHour; // 영업 시간1 (범위 시작)
    private int endHour; // 영업 시간2 (범위 끝)
}
