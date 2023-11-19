package cafegaza.cafegazaspring.service;

import cafegaza.cafegazaspring.domain.Bookmark;
import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.domain.Member;
import cafegaza.cafegazaspring.dto.BookmarkDto;
import cafegaza.cafegazaspring.dto.CafeDto;
import cafegaza.cafegazaspring.dto.MemberDto;
import cafegaza.cafegazaspring.repository.BookmarkRepository;
import cafegaza.cafegazaspring.repository.CafeRepository;
import cafegaza.cafegazaspring.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final CafeRepository cafeRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public BookmarkService(BookmarkRepository bookmarkRepository, CafeRepository cafeRepository, MemberRepository memberRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.cafeRepository = cafeRepository;
        this.memberRepository = memberRepository;
    }

    /*
        즐겨찾기 추가
     */
    public Long addBookmark(Long cafeId, Long memberId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(
                () -> new NoSuchElementException("[즐겨찾기]카페를 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new NoSuchElementException("[즐겨찾기]사용자를 찾을 수 없습니다."));

        // 즐겨찾기가 되어있지 않다면, 즐겨찾기 추가 실시
        if(bookmarkRepository.findByCafeAndMember(cafe, member) == null) {
            cafeRepository.updateBookmarkCountForAdd(cafe.getCafeId()); // 북마크 카운트 +1

            // 북마크 엔티티 생성
            Bookmark bookmark = Bookmark.builder()
                        .cafe(cafe)
                        .member(member)
                        .build();
            bookmarkRepository.save(bookmark); // 저장
            return bookmark.getId();
        }
        return null;
    }

    /*
        즐겨찾기 삭제
     */
    public void delBookmark(Long cafeId, Long memberId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(
                () -> new NoSuchElementException("[즐겨찾기]카페를 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new NoSuchElementException("[즐겨찾기]사용자를 찾을 수 없습니다."));

        // 즐겨찾기가 되어있다면, 즐겨찾기 삭제 실시
        Bookmark bookmark = bookmarkRepository.findByCafeAndMember(cafe, member); // 삭제 대상 북마크 엔티티 찾기
        if(bookmark != null) {
            cafeRepository.updateBookmarkCountForDel(cafe.getCafeId()); // 카페의 북마크 카운트 -1
            bookmarkRepository.delete(bookmark); // 북마크 삭제
        }
    }
}