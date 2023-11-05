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
    public boolean addBookmark(Long cafeId, Long memberId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(
                () -> new NoSuchElementException("[즐겨찾기]카페를 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new NoSuchElementException("[즐겨찾기]사용자를 찾을 수 없습니다."));

        // 즐겨찾기가 되어있지 않다면, 즐겨찾기 추가 실시
        if(bookmarkRepository.findByCafeAndMember(cafe, member) == null) {
            cafeRepository.updateBookmarkCountForAdd(cafe.getCafeId());

            BookmarkDto bookmarkDto = new BookmarkDto();
            bookmarkDto.setCafe(CafeDto.toDto(cafe)); bookmarkDto.setMember(MemberDto.toDto(member));
            bookmarkRepository.save(bookmarkDto.toEntity());

            return true;

        } else return false;
    }

    /*
        즐겨찾기 삭제
     */
    public boolean delBookmark(CafeDto cafe, MemberDto member) {
        cafeRepository.findById(cafe.getId()).orElseThrow(
                () -> new NoSuchElementException("[즐겨찾기]카페를 찾을 수 없습니다."));
        memberRepository.findById(member.getId()).orElseThrow(
                () -> new NoSuchElementException("[즐겨찾기]사용자를 찾을 수 없습니다."));

        // 즐겨찾기가 되어있다면, 즐겨찾기 삭제 실시
        if(bookmarkRepository.findByCafeAndMember(cafe.toEntity(), member.toEntity()) != null) {
            cafeRepository.updateBookmarkCountForDel(cafe.getId());

            Bookmark bookmark = bookmarkRepository.findByCafeAndMember(cafe.toEntity(), member.toEntity());
            bookmarkRepository.delete(bookmark);

            return true;

        } else return false;
    }
}