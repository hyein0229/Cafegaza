package cafegaza.cafegazaspring.repository;

import cafegaza.cafegazaspring.domain.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CafeRepository extends JpaRepository<Cafe, Long>, CafeRepositoryCustom {

    List<Cafe> findByDetailUrl(String detailUrl);

}
