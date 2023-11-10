package cafegaza.cafegazaspring.service;

import cafegaza.cafegazaspring.controller.SearchQuery;
import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.domain.Member;
import cafegaza.cafegazaspring.domain.OpenHour;
import cafegaza.cafegazaspring.dto.CafeDto;
import cafegaza.cafegazaspring.dto.KakaoSearchApiResDto;
import cafegaza.cafegazaspring.repository.BookmarkRepository;
import cafegaza.cafegazaspring.repository.CafeRepository;
import cafegaza.cafegazaspring.repository.FileRepository;
import cafegaza.cafegazaspring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CafeSearchService {

    private final CafeRepository cafeRepository;
    private final KakaoOpenApiManager kakaoOpenApiManager;
    private final FileRepository fileRepository;
    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;

    /**
     *사용자가 입력한 질의어로 카페 검색
     */
    public Page<CafeDto> searchCafe(SearchQuery searchQuery, Long memberId, Pageable pageable) throws Exception {

        Member member = null;
        if(memberId != null) {
            member = memberRepository.findById(memberId).get();
        }

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
        double[] centerCoord = null;
        // 검색 대상 지역명 찾기
        if(!region.isEmpty()) {
            region = getTargetRegion(region);; // 검색 대상이 될 가장 구체적인 지역(주소)명 가져오기
            if(!region.matches(".+[시|군|구|읍|면|로|길|동|리]")) { // 지역명이 아닌 경우
                region = null;
                centerCoord = getCenterCoordinates(placeList);
            } else {
                region = region.replaceAll("[특별시|광역시]", ""); // db 조회를 위해 도시 이름의 경우 '~시' 또는 '서울' 형식으로 맞춤
            }
        }
        keyword = keyword.substring(0, keyword.length()-2).trim(); // 키워드에서 '카페' 제거

        // DB 조회 결과
        searchResult = cafeRepository.findByMultipleCond(region, keyword, centerCoord, searchQuery, pageable);

        List<CafeDto> resultDtos = new ArrayList<>();
        for(Cafe cafe: searchResult) {
            CafeDto cafeDto = CafeDto.toDto(cafe);
            cafeDto.setIsOpen(isCafeOpen(cafe));
            if(member != null && bookmarkRepository.findByCafeAndMember(cafe, member) != null) {
                cafeDto.setIsBookmark(true);
            } else {
                cafeDto.setIsBookmark(false);
            }
            resultDtos.add(cafeDto);
        }

        return new PageImpl<>(resultDtos, pageable, searchResult.getTotalElements());
    }

    // 모든 카페 좌표들의 중심 좌표 찾기
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

    public List<CafeDto> getPopularPlaces() {
        List<Cafe> result = cafeRepository.findPopularPlaces();
        return result.stream().map(cafe -> CafeDto.toDto(cafe)).toList();
    }

    public CafeDto findById(Long cafeId, Long memberId) {

        Cafe findCafe = cafeRepository.findById(cafeId).get();
        CafeDto cafeDto = CafeDto.toDto(findCafe);
        cafeDto.setIsOpen(isCafeOpen(findCafe));

        if(memberId == null) {
            cafeDto.setIsBookmark(false);
        } else {
            Member findMember = memberRepository.findById(memberId).get();
            if(bookmarkRepository.findByCafeAndMember(findCafe, findMember) != null) {
                cafeDto.setIsBookmark(true);
            } else {
                cafeDto.setIsBookmark(false);
            }
        }

        return cafeDto;
     }

     public Page<String> getImages(Long cafeId, Pageable pageable) {
        Cafe findCafe = cafeRepository.findById(cafeId).get();
        return fileRepository.findFilePathByCafe(findCafe, pageable);

     }

     public boolean isCafeOpen(Cafe cafe) {
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

     public String getToday() {
         List<String> list = Arrays.asList("일", "월", "화", "수", "목", "금", "토", "일");
         Calendar cal = Calendar.getInstance();
         String dayOfWeek = list.get(cal.get(Calendar.DAY_OF_WEEK) - 1); // 현재 요일 구하기, 1~7 -> 일, 월~금, 토
         return dayOfWeek;
     }


}
