package cafegaza.cafegazaspring.service;

import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.domain.Member;
import cafegaza.cafegazaspring.dto.CafeDto;
import cafegaza.cafegazaspring.repository.BookmarkRepository;
import cafegaza.cafegazaspring.repository.CafeRepository;
import cafegaza.cafegazaspring.repository.FileRepository;
import cafegaza.cafegazaspring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeService {

    private final CafeRepository cafeRepository;
    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;
    private final FileRepository fileRepository;

    /**
     * 카페 찾기
     */
    public CafeDto findById(Long cafeId, Long memberId) {

        Cafe findCafe = cafeRepository.findById(cafeId).get();
        CafeDto cafeDto = CafeDto.toDto(findCafe);

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

    /**
     * 페이지 단위 가게 사진 가져오기
     */
    public Page<String> getImages(Long cafeId, Pageable pageable) {
        Cafe findCafe = cafeRepository.findById(cafeId).get();
        return fileRepository.findFilePathByCafe(findCafe, pageable);
    }

    /**
     * 최신 인기 가게 가져오기
     */
    public List<CafeDto> getPopularPlaces() {
        List<Cafe> result = cafeRepository.findPopularPlaces();
        return result.stream().map(cafe -> CafeDto.toDto(cafe)).toList();
    }
}
