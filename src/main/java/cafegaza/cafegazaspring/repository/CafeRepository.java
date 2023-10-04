package cafegaza.cafegazaspring.repository;

import cafegaza.cafegazaspring.domain.Cafe;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CafeRepository extends JpaRepository<Cafe, Long>, CafeRepositoryCustom {

    List<Cafe> findByDetailUrl(String detailUrl);

    // 즐겨찾기 추가 시, bookmarkCount에 + 1
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Cafe c SET c.bookmarkCount= c.bookmarkCount+1 WHERE c.cafeId= :cafeId")
    int updateBookmarkCountForAdd(Long cafeId);

    // 즐겨찾기 삭제 시, bookmarkCount에 - 1
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Cafe c SET c.bookmarkCount= c.bookmarkCount-1 WHERE c.cafeId= :cafeId")
    int updateBookmarkCountForDel(Long cafeId);
}
