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

        if(bookmarkService.addBookmark(1L, 9999L)) {
            System.out.println("bookmark add success");
        }
        else {
            System.out.println("bookmark add failed");
        }
    }

    @Test
    public void 즐겨찾기_삭제() {

        bookmarkService.addBookmark(1L, 9999L);

        if(bookmarkService.delBookmark(1L, 9999L)) {
            System.out.println("bookmark del success");
        }
        else {
            System.out.println("bookmark del failed");
        }
    }
}
