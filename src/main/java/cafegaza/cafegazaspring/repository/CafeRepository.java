package cafegaza.cafegazaspring.repository;

import cafegaza.cafegazaspring.domain.Cafe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CafeRepository extends JpaRepository<Cafe, Long>, CafeRepositoryCustom {

    List<Cafe> findByDetailUrl(String detailUrl);
    Page<Cafe> findByNameContains(String keyword, Pageable pageable); // 특정 키워드(카페명)로 찾기
    Page<Cafe> findByRoadAddressContainsOrAddressContains(String region1, String region2, Pageable pageable);

    @Query(value = "SELECT * FROM cafe WHERE (6371*acos(cos(radians(:lat))*cos(radians(y))*cos(radians(x)-radians(:lng))+sin(radians(:lat))*sin(radians(y)))) < 1 "
            , nativeQuery = true)
    Page<Cafe> findByDistance(@Param("lat") double lat, @Param("lng") double lng, Pageable pageable); // 기준 좌표로부터 1km 반경 내에 있는 카페 찾기

    // 현재 기준 좌표의 반경 1km 내의 카페 찾기
    @Query(value = "SELECT * FROM cafe WHERE (6371*acos(cos(radians(:lat))*cos(radians(y))*cos(radians(x)-radians(:lng))+sin(radians(:lat))*sin(radians(y)))) < 1 "
            +"AND name LIKE CONCAT('%', :keyword, '%')"
            , nativeQuery = true) // nativeQuery -> jpql 대신 sql 직접 사용
    Page<Cafe> findByDistanceAndNameContains(@Param("lat") double lat, @Param("lng") double lng, @Param("keyword") String keyword, Pageable pageable); // 반경 + 키워드


}
