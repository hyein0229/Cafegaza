package cafegaza.cafegazaspring.controller;

import lombok.Getter;
import lombok.Setter;

// 검색 시 ajax 쿼리를 받을 dto class
@Getter
@Setter
public class SearchQuery {
    private String keyword; // 키워드
    /*
        차후에 옵션에 대한 필드 추가 필요
     */
}
