package cafegaza.cafegazaspring.service;

import cafegaza.cafegazaspring.dto.ReviewForm;
import cafegaza.cafegazaspring.domain.Review;
import cafegaza.cafegazaspring.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

//@AutoConfigureMockMvc // MockMvc 를 사용하여 multipart/form-data 테스트
@SpringBootTest
@Transactional // 트랜잭션 실행 후 롤백
public class ReviewServiceTest {

    MockMvc mockMvc;
    @Autowired ReviewService reviewService;
    @Autowired ReviewRepository reviewRepository;
    /*
        리뷰 생성 테스트
     */
    @Test
    @Rollback(value = false)
    public void createReviewTest() throws Exception {

        // 리뷰 폼 작성
        Long cafeId = Long.valueOf(2L); // 리뷰 대상 카페 id
        ReviewForm reviewForm = new ReviewForm();
        reviewForm.setContent("wgwgwwegwefsgdsdsssssssssssssssssssssssssssssssssss" +
                "wegggggggggg");
        reviewForm.setRate(5);
        reviewForm.setImageFiles(new ArrayList<>());

        reviewService.create(cafeId, 9999L, reviewForm);

    }
    /*
        리뷰 수정 테스트
     */
    @Test
    @Rollback(value = false)
    public void updateReviewTest() throws Exception {

        // 기존 리뷰를 찾아 수정
        Review findReview = reviewService.findOne(49952L); // id 2번 리뷰 수정
        ReviewForm reviewForm = new ReviewForm();
        reviewForm.setId(findReview.getReviewId());
        reviewForm.setContent("리뷰 수정 테스트입니다.");
        reviewForm.setRate(3);
        reviewForm.setImageFiles(new ArrayList<>());

        // 리뷰 수정
        reviewService.update(49952L, reviewForm);

    }

    /*
        리뷰 삭제 테스트
     */
    @Test
    @Rollback(value = false)
    public void deleteReviewTest() {
        reviewService.delete(49952L);

    }

}
