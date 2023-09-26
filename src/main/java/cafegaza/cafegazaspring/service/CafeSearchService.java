package cafegaza.cafegazaspring.service;

import cafegaza.cafegazaspring.controller.SearchQuery;
import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.domain.Menu;
import cafegaza.cafegazaspring.dto.CafeDto;
import cafegaza.cafegazaspring.dto.KakaoSearchApiResDto;
import cafegaza.cafegazaspring.repository.CafeRepository;
import cafegaza.cafegazaspring.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeSearchService {

    private final CafeRepository cafeRepository;
    private final MenuRepository menuRepository;
    private final KakaoOpenApiManager kakaoOpenApiManager;

    /**
     *사용자가 입력한 질의어로 카페 검색
     */
    public Page<CafeDto> searchCafe(SearchQuery searchQuery, Pageable pageable) throws Exception {

        // 검색할 키워드
        Page<Cafe> searchResult = null;
        String query = searchQuery.getKeyword();
        if(!query.contains(" 카페")) { // 카페 검색을 위한 처리
            query += " 카페";
        }

        // 카카오맵에서의 검색 결과를 활용하기 위해 api 호출
        KakaoSearchApiResDto apiSearchResult = kakaoOpenApiManager.kakaomapSearchApi(query);

        // api 에서의 검색 결과, 검색에 사용된 지역 정보, 키워드 가져오기
        List<KakaoSearchApiResDto.Place> placeList = apiSearchResult.getDocuments(); // 검색된 장소 목록
        String region = apiSearchResult.getMeta().getSame_name().getSelected_region(); // 지역 정보
        String keyword = apiSearchResult.getMeta().getSame_name().getKeyword(); // 특정 키워드

        // 검색 대상 지역명 찾기
        if(!region.isEmpty()) {
            region = getTargetRegion(region); // 검색 대상이 될 가장 구체적인 지역(주소)명 가져오기
        }
        keyword = keyword.substring(0, keyword.length()-2).trim(); // 키워드에서 '카페' 제거

        // 다중 조건 검색 타입 구하여 db 조회
        String searchType = getSearchType(region, keyword, searchQuery);
        region = region.replaceAll("[특별시|광역시]", ""); // db 조회를 위해 도시 이름의 경우 '~시' 또는 '서울' 형식으로 맞춤
        System.out.println(searchType);
        switch (searchType) {
            case "r": // 지역명
                searchResult = cafeRepository.findByRoadAddressContainsOrAddressContains(region, region, pageable);
                break;
            case "rk": // 지역명 + 키워드
                searchResult = cafeRepository.findByRegionKeyword(region, keyword, pageable);
                break;
            case "rm": // 지역명 + 메뉴
                searchResult = cafeRepository.findByRegionMenu(region, searchQuery.getMenuOption(), pageable);
                break;
            case "rkm": // 지역명 + 키워드 + 메뉴
                searchResult = cafeRepository.findByRegionkeywordMenu(region, keyword, searchQuery.getMenuOption(), pageable);
                break;
            case "d": // 반경 내 좌표
                double[] centerCoord = getCenterCoordinates(placeList);
                searchResult = cafeRepository.findByDistance(centerCoord[1], centerCoord[0], pageable);
                break;
            case "dk": // 반경 내 좌표 + 키워드
                centerCoord = getCenterCoordinates(placeList);
                searchResult = cafeRepository.findByDistanceAndNameContains(centerCoord[1], centerCoord[0], keyword, pageable);
                break;
            default: // 키워드 검색
                searchResult = cafeRepository.findByNameContains(keyword, pageable);
                break;
        }

        return new CafeDto().toDtoList(searchResult);
    }
    // 검색 타입 구하기
    private String getSearchType(String region, String keyword, SearchQuery searchQuery) {
        String type = "";
        if(!region.isEmpty()) {
            if(region.matches(".+[시|군|구|읍|면|로|길|동|리]")) { // 지역명인 경우
                type += "r";
            } else {
                type += "d"; // 반경 거리로 찾기
            }
        }
        type = (!keyword.isEmpty()) ? type + "k" : type;
        type = (!searchQuery.getMenuOption().isEmpty()) ? type + "m" : type;
        return type;
    }
    // 모든 카페 좌표들의 중심 좌표 찾기
    private double[] getCenterCoordinates(List<KakaoSearchApiResDto.Place> placeList) {
        double xSum = 0;
        double ySum = 0;
        for (int i = 0; i < placeList.size(); i++) {
            xSum += Double.parseDouble(placeList.get(i).getX());
            ySum += Double.parseDouble(placeList.get(i).getY());
        }
        // 평균을 구하여 중심 좌표 찾음
        double centerX = xSum / (double)placeList.size();
        double centerY = ySum / (double)placeList.size();
        return new double[] {centerX, centerY};
    }
    // 가장 구체적인 지역명 찾기
    private String getTargetRegion(String region) {
        String[] regionArr = region.split(" "); // ex) ['서울', '마포구' '합정역']
        String detailedAddr = regionArr[regionArr.length-1]; // 가장 구체적인 주소명
        return detailedAddr;
    }


    /**
     * List<> to Page<> 변환
     */
    public Page<Cafe> convertListToPage(List<Cafe> cafeList, Pageable pageable) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), cafeList.size());

        List<Cafe> pageContent = cafeList.subList(start, end); // 해당 페이지 번호에 해당하는 카페 목록
        return new PageImpl<>(pageContent, pageRequest, cafeList.size()); // Page<Cafe> 타입으로 반환
    }

}
