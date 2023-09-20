package cafegaza.cafegazaspring.service;

import cafegaza.cafegazaspring.controller.SearchQuery;
import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.dto.CafeDto;
import cafegaza.cafegazaspring.dto.SearchCafeResponseDto;
import cafegaza.cafegazaspring.dto.KakaoSearchApiResDto;
import cafegaza.cafegazaspring.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeSearchService {

    private final CafeRepository cafeRepository;
    private final KakaoOpenApiManager kakaoOpenApiManager;

    /*
        사용자가 입력한 질의어로 카페 검색
     */
    public SearchCafeResponseDto searchCafe(SearchQuery searchQuery, Pageable pageable) throws Exception {

        String query = searchQuery.getKeyword();
        SearchCafeResponseDto searchCafeResponseDto = new SearchCafeResponseDto();
        Page<Cafe> searchResult = null; // 검색 결과
        if(!query.contains(" 카페")) { // 카페 검색을 위함
            query += " 카페";
        }

        // 카카오맵에서의 검색 결과를 활용하기 위해 api 호출
        KakaoSearchApiResDto apiSearchResult = kakaoOpenApiManager.kakaomapSearchApi(query);

        // 첫번째, 지역에 해당하는 카페 찾기
        String region = apiSearchResult.getMeta().getSame_name().getSelected_region(); // api 에서 검색에 사용된 지역 정보 가져오기
        String keyword = apiSearchResult.getMeta().getSame_name().getKeyword(); // api 에서 검색에 사용된 키워드 가져오기
        keyword = keyword.substring(0, keyword.length()-2).trim(); // 키워드에서 '카페' 제거

        if(!region.isEmpty()){ // 검색된 지역 정보가 있다면

            String[] regionArr = region.split(" "); // ex) ['서울', '마포구' '합정역']
            String detailedAddr = regionArr[regionArr.length-1]; // 주소는 뒤로 갈수록 구체적이므로 가장 뒤의 단어를 가져옴
            int addrOpt = 0;

            if (detailedAddr.matches(".+[시|군|구|읍|면|로|길]")) { // 가장 구체적 주소가 시/군/구 또는 도로명 주소에 포함되는 경우
                if (detailedAddr.matches((".+[특별시|광역시]"))) {
                    detailedAddr = detailedAddr.replaceAll("특별시", "").replaceAll("광역시", "");
                }
                addrOpt = 1;

            } else if (detailedAddr.matches(".+[동|리]")) { // 지번 주소에 포함되는 경우
                addrOpt = 2;
            }

            if (!keyword.isEmpty() && addrOpt == 1) { // 도로명 주소 + 특정 키워드로 찾기
                searchResult = cafeRepository.findByRoadAddressContainingAndNameContaining(detailedAddr, keyword, pageable);
                
            } else if(!keyword.isEmpty() && addrOpt == 2){ // 지번 주소 + 특정 키워드로 찾기
                searchResult = cafeRepository.findByAddressContainingAndNameContaining(detailedAddr, keyword, pageable);

            } else if(addrOpt == 1) { // 도로명 주소로 찾기
                searchResult = cafeRepository.findByRoadAddressContaining(detailedAddr, pageable);

            } else if(addrOpt == 2) { // 지번 주소로 찾기
                searchResult = cafeRepository.findByAddressContaining(detailedAddr, pageable);

            } else { // addrOpt = 0 인 경우

                // 지역명으로 검색 결과를 찾지 못한 경우
                // 카카오맵에서 검색된 카페 좌표들의 중심을 찾고,
                // 반경 1km 안의 카페들을 찾기
                List<KakaoSearchApiResDto.Place> placeList = apiSearchResult.getDocuments(); // api 에서 응답된 카페 정보 가져오기

                double xSum = 0;
                double ySum = 0;
                for (int i = 0; i < placeList.size(); i++) {
                    xSum += Double.parseDouble(placeList.get(i).getX());
                    ySum += Double.parseDouble(placeList.get(i).getY());
                }
                // 평균을 구하여 중심 좌표 찾음
                double centerX = xSum / (double)placeList.size();
                double centerY = ySum / (double)placeList.size();

                if (!keyword.isEmpty()) {
                    searchResult = cafeRepository.findByDistanceAndNameContaining(centerY, centerX, keyword, pageable);
                } else {
                    searchResult = cafeRepository.findByDistance(centerY, centerX, pageable);
                }
            }

        } else{ // 키워드로만 찾기
            searchResult = cafeRepository.findByNameContaining(keyword, pageable);
        }

        searchCafeResponseDto.setContent(searchResult.getContent().stream().map(cafe -> CafeDto.toDto(cafe)).toList()); // Cafe entity -> dto 로 변환
        searchCafeResponseDto.setTotalElements(searchResult.getTotalElements());
        searchCafeResponseDto.setTotalPages(searchResult.getTotalPages());

        return searchCafeResponseDto;
    }

}
