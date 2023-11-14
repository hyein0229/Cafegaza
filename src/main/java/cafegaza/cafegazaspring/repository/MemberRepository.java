package cafegaza.cafegazaspring.repository;

import cafegaza.cafegazaspring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findById(Long id);
    Optional<Member> findByUserId(String userId);

    // userId 중복 확인
    // userId가 중복된다면 1을, 중복되지 않는다면 0을 반환
    @Query(value = "SELECT COUNT(*) FROM Member WHERE user_id = :userId", nativeQuery = true)
    int isExistUserId(@Param(value = "userId") String userId);

    // nickname 중복 확인
    // nickname이 중복된다면 1을, 중복되지 않는다면 0을 반환
    @Query(value = "SELECT COUNT(*) FROM Member WHERE nickname = :nickname", nativeQuery = true)
    int isExistNickname(@Param(value = "nickname") String nickname);

}