package cafegaza.cafegazaspring.domain.uploadFile;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFile is a Querydsl query type for File
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFile extends EntityPathBase<File> {

    private static final long serialVersionUID = -78259082L;

    public static final QFile file = new QFile("file");

    public final StringPath fileOrigName = createString("fileOrigName");

    public final StringPath filePath = createString("filePath");

    public final StringPath fileStoredName = createString("fileStoredName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QFile(String variable) {
        super(File.class, forVariable(variable));
    }

    public QFile(Path<? extends File> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFile(PathMetadata metadata) {
        super(File.class, metadata);
    }

}

