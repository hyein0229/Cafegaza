package cafegaza.cafegazaspring.service;

import cafegaza.cafegazaspring.controller.ReviewForm;
import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.domain.Review;
import cafegaza.cafegazaspring.repository.ReviewRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

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
        Long cafeId = Long.valueOf(1); // 리뷰 대상 카페 id 1번 -> 어반플랜트 합정
        ReviewForm reviewForm = new ReviewForm();
        reviewForm.setContent("리뷰 생성 테스트");
        reviewForm.setRate(3);
        reviewForm.setImageFiles(new ArrayList<>());

        String fileName = "KakaoTalk_20230813_015136695.jpg";
        String contentType = "jpg";
        String filePath = "src/test/testResources/KakaoTalk_20230813_015136695.jpg"; // 파일 경로
        FileInputStream fileInputStream = new FileInputStream(filePath);

        List<MultipartFile> imageFiles = new ArrayList<>();
        MultipartFile image = new MockMultipartFile(
                "images",
                fileName,
                contentType,
                fileInputStream
        );
        imageFiles.add(image);
        reviewForm.setImageFiles(imageFiles);

        // 리뷰 생성했을 때
        Long savedId = reviewService.create(cafeId, reviewForm);

        //then
        Review review = reviewRepository.findById(savedId).get();
        Cafe cafe = review.getCafe();
        Assertions.assertEquals(review, cafe.getReviews().get(0)); // 연관관계 매핑 확인

    }
    /*
        리뷰 수정 테스트
     */
    @Test
    @Rollback(value = false)
    public void updateReviewTest() throws Exception {

        // 기존 리뷰를 찾아 수정
        Review findReview = reviewService.findOne(52L); // id 2번 리뷰 수정
        ReviewForm reviewForm = new ReviewForm();
        reviewForm.setId(findReview.getReviewId());
        reviewForm.setContent("리뷰 수정 테스트입니다.");
        reviewForm.setRate(findReview.getRate());

        String fileName = "캡처.PNG";
        String contentType = "PNG";
        String filePath = "src/test/testResources/캡처.PNG"; // 파일 경로
        FileInputStream fileInputStream = new FileInputStream(filePath);

        List<MultipartFile> imageFiles = new ArrayList<>();
        MultipartFile image = new MockMultipartFile(
                            "images",
                            fileName,
                            contentType,
                            fileInputStream
                        );
        imageFiles.add(image);
        reviewForm.setImageFiles(imageFiles);

        // 리뷰 수정
        reviewService.update(52L, reviewForm);

        // then
        Review updatedReview = reviewRepository.findById(52L).get();
        Assertions.assertEquals("리뷰 수정 테스트입니다.", updatedReview.getContent());
        Assertions.assertEquals("캡처.PNG", updatedReview.getReviewImages().get(0).getFileOrigName());

    }

    /*
        리뷰 삭제 테스트
     */
    @Test
    @Rollback(value = false)
    public void deleteReviewTest() {
        reviewService.delete(52L);
    }

}
