package cafegaza.cafegazaspring.repository;

import cafegaza.cafegazaspring.controller.SearchQuery;
import cafegaza.cafegazaspring.domain.Cafe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CafeRepositoryCustom {

    Page<Cafe> findByMultipleCond(String region, String keyword, double[] centerCoord, SearchQuery searchQuery, Pageable pageable); // 다중 조건 검색
}
