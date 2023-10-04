package cafegaza.cafegazaspring.domain;

import cafegaza.cafegazaspring.domain.uploadFile.ReviewImage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review extends BaseTime {

    @Id @GeneratedValue
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe; // 리뷰 대상 카페

    private String content; // 내용

    private int rate; // 별점

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL) // 리뷰 이미지도 함께 저장
    @Builder.Default // builder 사용 시 초기화할 수 있도록 함
    private List<ReviewImage> reviewImages = new ArrayList<>();// 리뷰 이미지


    //--양방향 연관관계 매핑 메소드--//
    public void addReviewImage(ReviewImage reviewImage) {
        this.reviewImages.add(reviewImage);
        reviewImage.setReview(this);
    }
    public void setCafe(Cafe cafe){
        this.cafe = cafe;
        cafe.getReviews().add(this);
    }

    //-- 리뷰 생성 메소드--//
    public static Review createReview(Cafe cafe, String content, int rate, List<ReviewImage> reviewImages) {
        Review review = Review.builder().content(content).rate(rate).reviewImages(new ArrayList<>()).build();
        review.setCafe(cafe);
        for(ReviewImage reviewImage : reviewImages) {
            review.addReviewImage(reviewImage);
        }
        return review;
    }

    //-- 리뷰 수정 메소드 -- //
    public void update(String content, int rate, List<ReviewImage> reviewImages) {
        this.content = content;
        this.rate = rate;
        for(ReviewImage reviewImage : this.reviewImages) {
            reviewImage.setReview(null); // 기존 이미지 엔티티의 참조 해제
        }
        this.reviewImages.clear();
        for(ReviewImage reviewImage : reviewImages) {
            this.addReviewImage(reviewImage); // 새로운 이미지 추가
        }
    }



}
