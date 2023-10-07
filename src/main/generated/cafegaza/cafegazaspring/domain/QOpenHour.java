package cafegaza.cafegazaspring.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOpenHour is a Querydsl query type for OpenHour
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOpenHour extends EntityPathBase<OpenHour> {

    private static final long serialVersionUID = -1429267227L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOpenHour openHour = new QOpenHour("openHour");

    public final QBaseTime _super = new QBaseTime(this);

    public final NumberPath<Integer> breakEnd = createNumber("breakEnd", Integer.class);

    public final NumberPath<Integer> breakStart = createNumber("breakStart", Integer.class);

    public final QCafe cafe;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath day = createString("day");

    public final NumberPath<Integer> endTime = createNumber("endTime", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final NumberPath<Integer> startTime = createNumber("startTime", Integer.class);

    public QOpenHour(String variable) {
        this(OpenHour.class, forVariable(variable), INITS);
    }

    public QOpenHour(Path<? extends OpenHour> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOpenHour(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOpenHour(PathMetadata metadata, PathInits inits) {
        this(OpenHour.class, metadata, inits);
    }

    public QOpenHour(Class<? extends OpenHour> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cafe = inits.isInitialized("cafe") ? new QCafe(forProperty("cafe")) : null;
    }

}

