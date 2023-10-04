package cafegaza.cafegazaspring.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = 182006447L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final QBaseTime _super = new QBaseTime(this);

    public final QCafe cafe;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final NumberPath<Integer> rate = createNumber("rate", Integer.class);

    public final NumberPath<Long> reviewId = createNumber("reviewId", Long.class);

    public final ListPath<cafegaza.cafegazaspring.domain.uploadFile.ReviewImage, cafegaza.cafegazaspring.domain.uploadFile.QReviewImage> reviewImages = this.<cafegaza.cafegazaspring.domain.uploadFile.ReviewImage, cafegaza.cafegazaspring.domain.uploadFile.QReviewImage>createList("reviewImages", cafegaza.cafegazaspring.domain.uploadFile.ReviewImage.class, cafegaza.cafegazaspring.domain.uploadFile.QReviewImage.class, PathInits.DIRECT2);

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cafe = inits.isInitialized("cafe") ? new QCafe(forProperty("cafe")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

