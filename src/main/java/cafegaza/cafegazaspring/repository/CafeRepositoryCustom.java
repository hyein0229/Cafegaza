package cafegaza.cafegazaspring.repository;

import cafegaza.cafegazaspring.dto.SearchQuery;
import cafegaza.cafegazaspring.domain.Cafe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CafeRepositoryCustom {

    Page<Cafe> findByMultipleCond(SearchQuery searchQuery, Pageable pageable); // 다중 조건 검색

    List<Cafe> findPopularPlaces();
}
