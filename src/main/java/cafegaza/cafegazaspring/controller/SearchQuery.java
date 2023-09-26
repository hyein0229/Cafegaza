package cafegaza.cafegazaspring.controller;

import lombok.Getter;
import lombok.Setter;

// 검색 시 ajax 쿼리를 받을 dto class
@Getter
@Setter
public class SearchQuery {
    private String keyword; // 검색 키워드
    private String menuOption; // 메뉴 이름 검색
    //private int type; // 옵션 지정 타입
//    private double priceOption; // 가격 옵션
    /*
        차후에 옵션에 대한 필드 추가 필요
     */
}
