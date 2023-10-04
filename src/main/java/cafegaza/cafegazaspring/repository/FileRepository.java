package cafegaza.cafegazaspring.repository;

import cafegaza.cafegazaspring.domain.uploadFile.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<UploadFile, Long> {
}
