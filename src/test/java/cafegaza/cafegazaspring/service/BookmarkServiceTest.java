package cafegaza.cafegazaspring.service;

import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.domain.Member;
import cafegaza.cafegazaspring.dto.CafeDto;
import cafegaza.cafegazaspring.dto.MemberDto;
import cafegaza.cafegazaspring.repository.CafeRepository;
import cafegaza.cafegazaspring.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class BookmarkServiceTest {

    @Autowired BookmarkService bookmarkService;
    @Autowired MemberRepository memberRepository;
    @Autowired CafeRepository cafeRepository;

    @Test
    public void 즐겨찾기_추가() {
        Cafe cafe = cafeRepository.findById(1L).orElseThrow();
        Member member = memberRepository.findById(68L).orElseThrow();  // test를 위해 로컬에 임의로 생성해 놓은 데이터 사용

        if(bookmarkService.addBookmark(cafe.getCafeId(), member.getId())) {
            System.out.println("bookmark add success");
        }
        else {
            System.out.println("bookmark add failed");
        }
    }

    @Test
    public void 즐겨찾기_삭제() {
        Cafe cafe = cafeRepository.findById(1L).orElseThrow();
        Member member = memberRepository.findById(68L).orElseThrow();

        bookmarkService.addBookmark(cafe.getCafeId(), member.getId());

        if(bookmarkService.delBookmark(CafeDto.toDto(cafe), MemberDto.toDto(member))) {
            System.out.println("bookmark del success");
        }
        else {
            System.out.println("bookmark del failed");
        }
    }
}
