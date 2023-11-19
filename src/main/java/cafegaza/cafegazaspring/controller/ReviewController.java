package cafegaza.cafegazaspring.controller;

import cafegaza.cafegazaspring.dto.ReviewDto;
import cafegaza.cafegazaspring.dto.ReviewForm;
import cafegaza.cafegazaspring.service.CafeService;
import cafegaza.cafegazaspring.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final CafeService cafeService;

    /**
        해당 페이지의 리뷰 목록 가져오기
     */
    @ResponseBody
    @GetMapping("/review/{cafeId}") // ?page=0&size=15
    public Page<ReviewDto> reviewList(@PathVariable("cafeId") Long cafeId, Pageable pageable) {
        return reviewService.findList(cafeId, pageable);
    }


    /**
        작성된 리뷰 폼을 받아 리뷰 생성
     */
    @ResponseBody
    @PostMapping("/review/{cafeId}/new")
    public ResponseEntity createReview(@SessionAttribute(name = "sessionId", required = false) Long memberId, @PathVariable("cafeId") Long cafeId, @RequestBody ReviewForm reviewForm) throws Exception { // 작성자 정보도 필요System.out.println("test");
         // 로그인 페이지 구현된 후 다시 추가
//        if(memberId == null) {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 권한 없음
//        }

        ReviewDto createdReview = reviewService.create(cafeId, 9999L, reviewForm); // 임시 memberId 를 9999L 로 지정
        return new ResponseEntity<>(createdReview, HttpStatus.OK); // 성공적으로 요청 처리됨
    }

    /**
     리뷰 수정
     */
    @ResponseBody
    @PostMapping("/review/{reviewId}/edit")
    public ResponseEntity updateReview(@SessionAttribute(name = "sessionId", required = false) Long memberId, @PathVariable("reviewId") Long reviewId, @RequestBody ReviewForm reviewForm) throws Exception {

//        if(memberId == null) {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 권한 없음
//        }

        ReviewDto updatedReview = reviewService.update(reviewId, reviewForm);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);

    }

    /**
     리뷰 삭제
     */
    @GetMapping("/review/{reviewId}/delete")
    public String deleteReview(@PathVariable("reviewId") Long reviewId) {

        reviewService.delete(reviewId);
        return "redirect:/";

    }


}
