package cafegaza.cafegazaspring.service;

import cafegaza.cafegazaspring.dto.SearchQuery;
import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.domain.Member;
import cafegaza.cafegazaspring.dto.CafeDto;
import cafegaza.cafegazaspring.dto.KakaoSearchApiResDto;
import cafegaza.cafegazaspring.repository.BookmarkRepository;
import cafegaza.cafegazaspring.repository.CafeRepository;
import cafegaza.cafegazaspring.repository.FileRepository;
import cafegaza.cafegazaspring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final CafeRepository cafeRepository;
    private final KakaoOpenApiManager kakaoOpenApiManager;
    private final FileRepository fileRepository;
    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;

    /**
     *사용자가 입력한 질의어로 카페 검색
     */
    public Page<CafeDto> searchCafe(SearchQuery searchQuery, Long memberId, Pageable pageable) throws Exception {

        // 검색한 사용자 찾기
        Member member = (memberId != null) ? memberRepository.findById(memberId).get() : null;

        // 카카오맵에서의 검색 결과를 활용하기 위해 api 호출
        KakaoSearchApiResDto apiSearchResult = kakaoOpenApiManager.kakaomapSearchApi(searchQuery.getKeyword());

        // api 에서의 검색 결과, 검색에 사용된 지역 정보, 키워드 가져와서 searchQuery 에 내용 삽입
        getSearchResultInfo(searchQuery, apiSearchResult);

        // DB 조회 결과
        Page<Cafe> searchResult = cafeRepository.findByMultipleCond(searchQuery, pageable);

        return toDtos(searchResult, member, pageable);
    }

    // api 검색 결과로부터 검색에 이용할 키워드, 지역 정보 얻기
    private void getSearchResultInfo(SearchQuery searchQuery, KakaoSearchApiResDto apiSearchResult) {
        List<KakaoSearchApiResDto.Place> placeList = apiSearchResult.getDocuments(); // 검색된 장소 목록
        String region = apiSearchResult.getMeta().getSame_name().getSelected_region(); // 지역 정보
        String keyword = apiSearchResult.getMeta().getSame_name().getKeyword(); // 특정 키워드

        // 검색 대상 지역명 찾기
        if(!region.isEmpty()) {
            region = getTargetRegion(region);; // 검색 대상이 될 가장 구체적인 지역(주소)명 가져오기
            if(!region.matches(".+[시|군|구|읍|면|로|길|동|리]")) { // 행정 구역이 아닌 특정 건물이나 역 이름인 경우
                searchQuery.setRegion(null);
                searchQuery.setCenterCoord(getCenterCoordinates(placeList)); // 검색된 지점들의 중심 좌표를 이용
            } else {
                searchQuery.setRegion(region.replaceAll("특별시", "")); // '서울' 형식으로 맞춤
            }
        }
        searchQuery.setKeyword(keyword.substring(0, keyword.length()-2).trim()); // 키워드에서 '카페' 제거
    }

    // 모든 카페 지점들의 중심 좌표 찾기
    private double[] getCenterCoordinates(List<KakaoSearchApiResDto.Place> placeList) {
        if(!placeList.isEmpty()) {
            double xSum = 0;
            double ySum = 0;
            for (int i = 0; i < placeList.size(); i++) {
                xSum += Double.parseDouble(placeList.get(i).getX());
                ySum += Double.parseDouble(placeList.get(i).getY());
            }
            // 평균을 구하여 중심 좌표 찾음
            double centerX = xSum / (double) placeList.size();
            double centerY = ySum / (double) placeList.size();
            return new double[]{centerX, centerY};
        }
        return null;
    }

    // 가장 구체적인 지역명 찾기
    private String getTargetRegion(String region) {
        String[] regionArr = region.split(" "); // ex) ['서울', '마포구' '합정역']
        String detailedAddr = regionArr[regionArr.length-1]; // 가장 구체적인 주소명
        return detailedAddr;
    }

    // Page<entity> -> Page<dto> 반환
    private Page<CafeDto> toDtos(Page<Cafe> searchResult, Member member, Pageable pageable) {
        List<CafeDto> searchResultDtos = new ArrayList<>();
        for(Cafe cafe: searchResult) {
            CafeDto cafeDto = CafeDto.toDto(cafe);
            if(member != null && bookmarkRepository.findByCafeAndMember(cafe, member) != null) {
                cafeDto.setIsBookmark(true);
            } else {
                cafeDto.setIsBookmark(false);
            }
            searchResultDtos.add(cafeDto);
        }
        return new PageImpl<>(searchResultDtos, pageable, searchResult.getTotalElements());
    }

}
