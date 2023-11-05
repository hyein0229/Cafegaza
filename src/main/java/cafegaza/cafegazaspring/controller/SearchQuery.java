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
    private int startHour = 0; // 영업 시간1 (범위 시작)
    private int endHour = 0; // 영업 시간2 (범위 끝)
    private int currentOpen = 0; // 영업 중인 카페
    private int dawnOpen = 0; // 새벽 운영
    private int sortType = 1; // 1: 별점 순, 2: 리뷰 순
}
