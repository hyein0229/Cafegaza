package cafegaza.cafegazaspring.repository;

import cafegaza.cafegazaspring.controller.SearchQuery;
import cafegaza.cafegazaspring.domain.Cafe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CafeRepositoryCustom {

    Page<Cafe> findByRegionMenu(String region, String menuName, Pageable pageable);
    Page<Cafe> findByRegionKeyword(String region, String keyword, Pageable pageable);
    Page<Cafe> findByRegionkeywordMenu(String region, String keyword, String menuName, Pageable pageable);
    //Page<Cafe> findByMultipleCond(String region, String keyword, SearchQuery searchQuery, Pageable pageable);
}
