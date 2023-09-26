package cafegaza.cafegazaspring.domain.uploadFile;

import cafegaza.cafegazaspring.domain.Review;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewImage extends UploadFile {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    //-- 연관관계 메소드 --//
    public void setReview(Review review) {
        this.review = review;
    }

}
