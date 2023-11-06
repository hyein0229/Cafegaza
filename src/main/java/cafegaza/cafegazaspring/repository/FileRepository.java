package cafegaza.cafegazaspring.repository;

import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.domain.uploadFile.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FileRepository extends JpaRepository<File, Long> {

    @Query("select r.fileStoredName from ReviewImage r where r.cafe= :cafe")
    Page<String> findFilePathByCafe(Cafe cafe, Pageable pageable);


}
