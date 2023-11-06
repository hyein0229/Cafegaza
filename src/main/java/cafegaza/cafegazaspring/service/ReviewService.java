package cafegaza.cafegazaspring.service;

import cafegaza.cafegazaspring.controller.ReviewForm;
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
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(
                () -> new NoSuchElementException("카페를 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        // 리뷰 이미지 엔티티 생성
        List<ReviewImage> reviewImages = new ArrayList<>();
        if (!reviewForm.getImageFiles().isEmpty()) { // 사용자가 업로드한 이미지가 있다면
            List<FileDto> fileDtoList = reviewForm.getImageFiles();
            for(int i=0; i<fileDtoList.size(); i++) {
                 ReviewImage reviewImage = (ReviewImage) fileRepository.findById(fileDtoList.get(i).getId()).get();
                 reviewImages.add(reviewImage);
            }
        }

        Review review = Review.createReview(cafe, member, reviewForm.getContent(), reviewForm.getRate(), reviewImages); // review 엔티티 생성
        reviewRepository.save(review); // review db 에 저장

        double rate = cafeRepository.findAvgRate(cafe);
        cafe.updateRate(Double.parseDouble(String.format("%.1f", rate)));

        return ReviewDto.toDto(review);
    }

    /**
        수정
     */
    @Transactional
    public ReviewDto update(Long reviewId, ReviewForm reviewForm) throws Exception {
        Review findReview = reviewRepository.findById(reviewId).get();

        // 리뷰 이미지 엔티티 생성
        List<ReviewImage> reviewImages = new ArrayList<>();
        if (!reviewForm.getImageFiles().isEmpty()) { // 사용자가 업로드한 이미지가 있다면
            List<FileDto> fileDtoList = reviewForm.getImageFiles();
            for(int i=0; i<fileDtoList.size(); i++) {
                ReviewImage reviewImage = (ReviewImage) fileRepository.findById(fileDtoList.get(i).getId()).get();
                reviewImages.add(reviewImage);
            }
        }

        findReview.update(reviewForm.getContent(), reviewForm.getRate(), reviewImages); // 리뷰 내용 갱신
        return ReviewDto.toDto(findReview);
    }

//    /**
//      multipartFile to review Image entity
//     */
//    private List<ReviewImage> multipartFileToReviewImage(List<MultipartFile> multipartFiles) throws Exception {
//
//        List<ReviewImage> reviewImages = new ArrayList<>();
//        for (MultipartFile multipartFile : multipartFiles) {
//            ReviewImage reviewImage = (ReviewImage) fileHandler.saveFiles(multipartFile); // 리뷰 이미지 저장
//            reviewImages.add(reviewImage);
//        }
//        return reviewImages;
//    }

    /**
     페이지별 리뷰 읽기 (가져오기)
     */
    public Page<ReviewDto> findList(Long cafeId, Pageable pageable) {

        Cafe findCafe = cafeRepository.findById(cafeId).get(); // 리뷰 대상 카페 엔티티
        Page<Review> reviews = reviewRepository.findByCafeOrderByCreatedDateDesc(findCafe, pageable);
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
            double rate = cafeRepository.findAvgRate(cafe);
            cafe.updateRate(Double.parseDouble(String.format("%.1f", rate)));
        } else{
            cafe.updateRate(0);
        }
    }

    public Review findOne(Long reviewId) {
        return reviewRepository.findById(reviewId).get();
    } // id 로 리뷰 찾기


}
