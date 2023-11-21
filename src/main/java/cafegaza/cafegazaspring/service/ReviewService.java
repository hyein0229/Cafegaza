package cafegaza.cafegazaspring.service;

import cafegaza.cafegazaspring.dto.ReviewForm;
import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.domain.Member;
import cafegaza.cafegazaspring.domain.Review;
import cafegaza.cafegazaspring.domain.uploadFile.ReviewImage;
import cafegaza.cafegazaspring.dto.FileDto;
import cafegaza.cafegazaspring.dto.ReviewDto;
import cafegaza.cafegazaspring.repository.CafeRepository;
import cafegaza.cafegazaspring.repository.FileRepository;
import cafegaza.cafegazaspring.repository.MemberRepository;
import cafegaza.cafegazaspring.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService { // 리뷰 작성, 수정, 삭제, 읽기 기능 필요

    private final ReviewRepository reviewRepository;
    private final CafeRepository cafeRepository;
    private final FileRepository fileRepository;
    private final MemberRepository memberRepository;

    /**
        생성
     */
    @Transactional
    public ReviewDto create(Long cafeId, Long memberId, ReviewForm reviewForm) throws Exception {
        // 대상 카페와 사용자 엔티티 찾기
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new NoSuchElementException("카페를 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        // 이미지 엔티티 가져오기
        List<ReviewImage> reviewImages = getReviewImages(reviewForm.getImageFiles());

        Review review = Review.createReview(cafe, member, reviewForm.getContent(), reviewForm.getRate(), reviewImages); // 리뷰 엔티티 생성
        reviewRepository.save(review); // DB 저장

        updateRate(cafe); // 별점 업데이트

        return ReviewDto.toDto(review);
    }

    /**
        수정
     */
    @Transactional
    public ReviewDto update(Long reviewId, ReviewForm reviewForm) throws Exception {
        // 기존 리뷰 가져오기
        Review findReview = reviewRepository.findById(reviewId).get();
        // 리뷰 이미지 엔티티
        List<ReviewImage> reviewImages = getReviewImages(reviewForm.getImageFiles());
        // 리뷰 내용 갱신
        findReview.update(reviewForm.getContent(), reviewForm.getRate(), reviewImages);

        updateRate(findReview.getCafe()); // 별점 업데이트

        return ReviewDto.toDto(findReview);
    }

    /**
        삭제
     */
    @Transactional
    public void delete(Long reviewId) {
        Review findReview = reviewRepository.findById(reviewId).orElseThrow(
                () -> new NoSuchElementException("리뷰를 찾을 수 없습니다."));

        Cafe cafe = findReview.getCafe(); // 리뷰를 작성한 가게 찾기
        findReview.delete(); // 연관관계 해제
        reviewRepository.delete(findReview); // 엔티티 삭제
        updateRate(cafe); // 별점 업데이트
    }

    // 리뷰 추가, 수정, 삭제로 인한 별점 업데이트
    private void updateRate(Cafe cafe) {
        // 별점 업데이트
        if(!cafe.getReviews().isEmpty()) {
            double rate = cafeRepository.findAvgRate(cafe); // 평균 별점 구하기
            cafe.updateRate(Double.parseDouble(String.format("%.1f", rate)));
        } else{ // 리뷰가 없다면 0으로 초기화
            cafe.updateRate(0);
        }
    }

    // 이미지 엔티티 가져오기
    private List<ReviewImage> getReviewImages(List<FileDto> imageFiles) {
        List<ReviewImage> reviewImages = new ArrayList<>();
        if (!imageFiles.isEmpty()) { // 사용자가 업로드한 이미지가 있다면
            List<FileDto> fileDtoList = imageFiles;
            for(int i=0; i<fileDtoList.size(); i++) {
                ReviewImage reviewImage = (ReviewImage) fileRepository.findById(fileDtoList.get(i).getId()).get(); // 미리 저장했던 이미지 엔티티 가져오기
                reviewImages.add(reviewImage);
            }
        }
        return reviewImages;
    }

    /**
     페이지별 리뷰 읽기 (가져오기)
     */
    public Page<ReviewDto> findList(Long cafeId, Pageable pageable) {
        Cafe cafe = cafeRepository.findById(cafeId).get(); // 리뷰 대상 카페 엔티티
        Page<Review> reviews = reviewRepository.findByCafeOrderByCreatedDateDesc(cafe, pageable); // 생성일 기준 내림차순으로 찾기
        return new ReviewDto().toDtoList(reviews);
    }

    public Review findOne(Long reviewId) {
        return reviewRepository.findById(reviewId).get();
    }
}
