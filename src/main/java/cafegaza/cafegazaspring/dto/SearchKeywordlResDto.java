package cafegaza.cafegazaspring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchKeywordlResDto {
    private List<Place> documents;
    private PlaceMeta meta;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Place {
        private String id;
        private String place_name;
        private String category_name;
        private String category_group_code;
        private String category_group_name;
        private String phone;
        private String address_name;
        private String road_address_name;
        private String x;
        private String y;
        private String place_url;
        private String distance;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlaceMeta {
        private int total_count;
        private int pageable_count;
        private boolean is_end;
        private RegionInfo same_name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegionInfo {
        // ex) '중앙로 맛집' 검색 시
        private List<String> region; // 질의어에서 인식된 지역 리스트, '중앙로'에 해당
        private String keyword; // 질의어에서 지역 정보를 제외한 키워드 '맛집'에 해당
        private String selected_region; // 인식된 지역 리스트 중, 현재 검색에 사용된 지역 정보
    }
}
