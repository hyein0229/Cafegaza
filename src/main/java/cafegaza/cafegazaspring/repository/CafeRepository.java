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
public interface CafeRepository extends JpaRepository<Cafe, Long> {

    Page<Cafe> findByNameContaining(String keyword, Pageable pageable);
    Page<Cafe> findByAddressContaining(String region, Pageable pageable); // 지번 주소로 찾기
    Page<Cafe> findByRoadAddressContaining(String region, Pageable pageable); // 도로명 주소로 찾기
    Page<Cafe> findByAddressContainingAndNameContaining(String region, String keyword, Pageable pageable); // 지번 주소 + 키워드
    Page<Cafe> findByRoadAddressContainingAndNameContaining(String region, String keyword, Pageable pageable); // 도로명 주소 + 키워드

    // 현재 기준 좌표의 반경 1km 내의 카페 찾기
    @Query(value = "SELECT * FROM cafe WHERE (6371*acos(cos(radians(:lat))*cos(radians(y))*cos(radians(x)-radians(:lng))+sin(radians(:lat))*sin(radians(y)))) < 1 "
            +"AND name LIKE CONCAT('%', :keyword, '%')"
            , nativeQuery = true) // nativeQuery -> jpql 대신 sql 직접 사용
    Page<Cafe> findByDistanceAndNameContaining(@Param("lat") double lat, @Param("lng") double lng, @Param("keyword") String keyword, Pageable pageable); // 반경 + 키워드

    @Query(value = "SELECT * FROM cafe WHERE (6371*acos(cos(radians(:lat))*cos(radians(y))*cos(radians(x)-radians(:lng))+sin(radians(:lat))*sin(radians(y)))) < 1 "
            , nativeQuery = true)
    Page<Cafe> findByDistance(@Param("lat") double lat, @Param("lng") double lng, Pageable pageable); // 반경

}
