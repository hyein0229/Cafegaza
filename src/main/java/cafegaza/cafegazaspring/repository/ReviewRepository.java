package cafegaza.cafegazaspring.repository;

import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByCafeOrderByCreatedDateAsc(Cafe cafe, Pageable pageable); // 페이지 사이즈만큼의 리뷰를 리뷰 생성일 기준 오름차순으로 반환
}
