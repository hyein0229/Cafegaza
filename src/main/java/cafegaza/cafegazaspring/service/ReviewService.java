package cafegaza.cafegazaspring.service;

import cafegaza.cafegazaspring.controller.ReviewForm;
import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.domain.Review;
import cafegaza.cafegazaspring.domain.uploadFile.ReviewImage;
import cafegaza.cafegazaspring.dto.ReviewDto;
import cafegaza.cafegazaspring.repository.CafeRepository;
import cafegaza.cafegazaspring.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService { // 리뷰 작성, 수정, 삭제, 읽기 기능 필요

    private final ReviewRepository reviewRepository;
    private final CafeRepository cafeRepository;
//    private final FileRepository fileRepository;
    private final FileHandler fileHandler;

    /**
        생성
     */
    @Transactional
    public Long create(Long cafeId, ReviewForm reviewForm) throws Exception {

        Cafe cafe = cafeRepository.findById(cafeId).get(); // 리뷰 대상 카페 엔티티

        // 리뷰 이미지 엔티티 생성
        List<ReviewImage> reviewImages = new ArrayList<>();
        if (!reviewForm.getImageFiles().isEmpty()){ // 사용자가 업로드한 이미지가 있다면
            reviewImages = multipartFileToReviewImage(reviewForm.getImageFiles());
        }

        Review review = Review.createReview(cafe, reviewForm.getContent(), reviewForm.getRate(), reviewImages); // review 엔티티 생성
        Long savedId = reviewRepository.save(review).getReviewId(); // review 를 db 에 저장

        double rate = reviewRepository.findAvgRate(cafe);
        cafe.updateRate(Double.parseDouble(String.format("%.1f", rate)));

        return savedId;
    }

    /**
        수정
     */
    @Transactional
    public void update(Long reviewId, ReviewForm reviewForm) throws Exception {
        Review findReview = reviewRepository.findById(reviewId).get();
        List<ReviewImage> newReviewImages = multipartFileToReviewImage(reviewForm.getImageFiles());

        findReview.update(reviewForm.getContent(), reviewForm.getRate(), newReviewImages); // 리뷰 내용 갱신
    }

    /**
      multipartFile to review Image entity
     */
    private List<ReviewImage> multipartFileToReviewImage(List<MultipartFile> multipartFiles) throws Exception {

        List<ReviewImage> reviewImages = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            ReviewImage reviewImage = (ReviewImage) fileHandler.saveFiles(multipartFile, 1); // 리뷰 이미지 저장
            reviewImages.add(reviewImage);
        }
        return reviewImages;
    }

    /**
     페이지별 리뷰 읽기 (가져오기)
     */
    public Page<ReviewDto> findList(Long cafeId, Pageable pageable) {

        Cafe findCafe = cafeRepository.findById(cafeId).get(); // 리뷰 대상 카페 엔티티
        Page<Review> reviews = reviewRepository.findByCafeOrderByCreatedDateAsc(findCafe, pageable);
        Page<ReviewDto> reviewDtos = new ReviewDto().toDtoList(reviews);
        return reviewDtos;

    }

    /**
        삭제
     */
    @Transactional
    public void delete(Long reviewId) {
        Review findReview = reviewRepository.findById(reviewId).orElseThrow(
                () -> new NoSuchElementException("리뷰를 찾을 수 없습니다."));;

//        if(!findReview.getReviewImages().isEmpty()){
//            fileRepository.deleteAll(findReview.getReviewImages());
//        }
        Cafe cafe = findReview.getCafe();
        findReview.delete();
        reviewRepository.delete(findReview);

        if(!cafe.getReviews().isEmpty()) {
            double rate = reviewRepository.findAvgRate(cafe);
            cafe.updateRate(Double.parseDouble(String.format("%.1f", rate)));
        } else{
            cafe.updateRate(0);
        }
    }

    public Review findOne(Long reviewId) {
        return reviewRepository.findById(reviewId).get();
    } // id 로 리뷰 찾기


}
