package cafegaza.cafegazaspring.repository;

import cafegaza.cafegazaspring.domain.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CafeRepository extends JpaRepository<Cafe, Long> {

    List<Cafe> findByNameContaining(String name);
    List<Cafe> findByAddressContaining(String region); // 지번 주소로 찾기
    List<Cafe> findByRoadAddressContaining(String region); // 도로명 주소로 찾기

}
