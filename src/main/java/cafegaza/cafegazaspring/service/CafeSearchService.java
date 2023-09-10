package cafegaza.cafegazaspring.service;

import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.dto.SearchKeywordlResDto;
import cafegaza.cafegazaspring.repository.CafeRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CafeSearchService {

    private final CafeRepository cafeRepository;
    private final KakaoOpenApiManager kakaoOpenApiManager;

    /*
        사용자가 입력한 질의어로 카페 검색
     */
    public List<Cafe> searchCafe(String query) throws Exception {

        List<Cafe> searchResult = new ArrayList<>(); // 검색 결과
        if(!query.contains(" 카페")) { // 카페 검색을 위함
            query += " 카페";
        }

        // 카카오맵에서의 검색 결과를 활용하기 위해 api 호출
        SearchKeywordlResDto apiSearchResult = kakaoOpenApiManager.kakaomapSearchApi(query);

        // 첫번째, 지역에 해당하는 카페 찾기
        String region = apiSearchResult.getMeta().getSame_name().getSelected_region(); // api 에서 검색에 사용된 지역 정보 가져오기
        List<Cafe> searchByRegionResult = new ArrayList<>();
        if(!region.isEmpty()){ // 검색된 지역 정보가 있다면
            searchByRegionResult = searchByRegion(region); // 지역명으로 카페 서치

            // 지역명으로 검색 결과를 찾지 못한 경우
            // 카카오맵에서 검색된 카페 좌표들의 중심을 찾고,
            // 반경 1km 안의 카페들을 찾기
            if (searchByRegionResult.isEmpty()) {
                List<SearchKeywordlResDto.Place> placeList = apiSearchResult.getDocuments(); // api 에서 응답된 카페 정보 가져오기

                double xSum = 0;
                double ySum = 0;
                for (int i = 0; i < placeList.size(); i++) {
                    xSum += Double.parseDouble(placeList.get(i).getX());
                    ySum += Double.parseDouble(placeList.get(i).getY());
                }

                // 평균을 구하여 좌표들의 중심 좌표 찾음
                double centerX = xSum / placeList.size();
                double centerY = ySum / placeList.size();

                List<Cafe> cafeList = cafeRepository.findAll();
                for (int i = 0; i < cafeList.size(); i++) {
                    double distance = getDistance(centerX, centerY, cafeList.get(i).getX(), cafeList.get(i).getY());
                    if (distance <= 1) { // 반경 1km 내의 있는 카페를 찾기
                        searchByRegionResult.add(cafeList.get(i));

                    }
                }
            }
        }

        // 두번째, 지역으로 찾은 카페 결과에서 키워드에 맞는 카페 찾기
        String keyword = apiSearchResult.getMeta().getSame_name().getKeyword();
        keyword = keyword.substring(0, keyword.length()-2);
        if (!keyword.trim().isEmpty()) { // '카페' 를 제외한 특정 키워드가 있다면
            searchResult.addAll(searchByKeyword(searchByRegionResult, keyword.trim()));
        } else{
            searchResult.addAll(searchByRegionResult);
        }

        if (searchResult.size() > 500) { // 최대 500개 검색 결과 제한
            searchResult = searchResult.subList(0, 500);
        }

        return searchResult;

    }

    /*
        지역과 연관된 카페 찾기
     */
    public List<Cafe> searchByRegion(String region){

        List<Cafe> searchResult = new ArrayList<>();
        String[] regionArr = region.split(" "); // ex) ['서울', '마포구' '합정역']
        String detailedAddr = regionArr[regionArr.length-1]; // 주소는 뒤로 갈수록 구체적이므로 가장 뒤의 단어를 가져옴

        if (detailedAddr.matches(".+[시|군|구|읍|면|로|길]")) { // 가장 구체적 주소가 시/군/구 또는 도로명 주소에 포함되는 경우
            if (detailedAddr.matches((".+[특별시|광역시]"))) {
                detailedAddr = detailedAddr.replaceAll("특별시", "").replaceAll("광역시", "");
            }
            searchResult.addAll(cafeRepository.findByRoadAddressContaining(detailedAddr)); // 도로명 주소에서 찾음

        } else if (detailedAddr.matches(".+[동|리]")) {
            searchResult.addAll(cafeRepository.findByAddressContaining(detailedAddr)); // 지번 주소에서 찾음
        }

        return searchResult;
    }

    /*
        키워드에 맞는 카페 찾기
     */
    public List<Cafe> searchByKeyword(List<Cafe> cafeList, String keyword) {

        List<Cafe> searchResult = new ArrayList<>();
        cafeList.forEach(cafe -> {

            if (cafe.getName().contains(keyword)) { // 카페명이 키워드를 포함하고 있다면 검색 결과에 포함
                searchResult.add(cafe);
            }
        });

        return searchResult;
    }

    /*
        경위도 좌표 사이의 거리 구하기 -> 다시 확인 필요
     */
    public double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double theta = lng1 - lng2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));

        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1609.344;

        return (Math.round(dist/10)*10) / 1000.0; // km 단위로 반환
    }
}
