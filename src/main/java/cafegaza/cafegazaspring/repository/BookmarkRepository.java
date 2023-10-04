package cafegaza.cafegazaspring.repository;

import cafegaza.cafegazaspring.domain.Bookmark;
import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Bookmark findByCafeAndMember(Cafe cafe, Member member);

}
