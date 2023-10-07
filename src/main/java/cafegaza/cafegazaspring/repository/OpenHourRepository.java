package cafegaza.cafegazaspring.repository;

import cafegaza.cafegazaspring.domain.OpenHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenHourRepository extends JpaRepository<OpenHour, Long> {
}
