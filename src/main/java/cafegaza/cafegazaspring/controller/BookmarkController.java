package cafegaza.cafegazaspring.controller;

import cafegaza.cafegazaspring.dto.CafeDto;
import cafegaza.cafegazaspring.service.BookmarkService;
import cafegaza.cafegazaspring.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailParseException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    /**
     * 북마크 추가하기
     */
    @PostMapping("/bookmark/{cafeId}")
    public ResponseEntity addBookMark(@SessionAttribute(name = "sessionId", required = false) Long memberId, @PathVariable("cafeId") Long cafeId) {
        if(memberId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 로그인이 되어있지 않아 권한 없음
        }

        bookmarkService.addBookmark(cafeId, memberId);
        return new ResponseEntity<>(HttpStatus.OK); // 성공적으로 요청 처리됨
    }

    /**
     * 북마크 삭제하기
     */
    @PostMapping("/bookmark/{cafeId}/delete")
    public ResponseEntity deleteBookMark(@SessionAttribute(name = "sessionId", required = false) Long memberId, @PathVariable("cafeId") Long cafeId) {
        if(memberId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 로그인이 되어있지 않아 권한 없음
        }

        bookmarkService.delBookmark(cafeId, memberId);
        return new ResponseEntity<>(HttpStatus.OK); // 성공적으로 요청 처리됨
    }

}
