package cafegaza.cafegazaspring.domain.uploadFile;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUploadFile is a Querydsl query type for UploadFile
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QUploadFile extends EntityPathBase<UploadFile> {

    private static final long serialVersionUID = -1091317097L;

    public static final QUploadFile uploadFile = new QUploadFile("uploadFile");

    public final StringPath fileOrigName = createString("fileOrigName");

    public final StringPath filePath = createString("filePath");

    public final StringPath fileStoredName = createString("fileStoredName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QUploadFile(String variable) {
        super(UploadFile.class, forVariable(variable));
    }

    public QUploadFile(Path<? extends UploadFile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUploadFile(PathMetadata metadata) {
        super(UploadFile.class, metadata);
    }

}

