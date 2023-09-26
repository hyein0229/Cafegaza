package cafegaza.cafegazaspring.dto;

import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.domain.Review;
import jakarta.persistence.AssociationOverride;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private Long reviewId;
    private Long cafeId;
    private Long memberId;
    private String nickname; // 작성자 이름
    private String content; // 리뷰 내용
    private double rate; // 별점
    private List<String> images; // 리뷰 이미지 저장명
    private LocalDateTime createdDate; // 작성일

    // entity -> dto
    public static ReviewDto toDto(Review review) {
        return ReviewDto.builder()
                .reviewId(review.getReviewId())
                .cafeId(review.getCafe().getCafeId())
                .memberId(review.getMember().getId())
                .nickname(review.getMember().getNickname())
                .content(review.getContent())
                .rate(review.getRate())
                .images(review.getReviewImages().stream().map(reviewImage -> reviewImage.getFileStoredName()).toList())
                .createdDate(review.getCreatedDate())
                .build();
    }

    // Page<Entity> -> Page<Dto> 변환
    public Page<ReviewDto> toDtoList(Page<Review> reviews) {
        return reviews.map(review -> toDto(review));
    }


}
