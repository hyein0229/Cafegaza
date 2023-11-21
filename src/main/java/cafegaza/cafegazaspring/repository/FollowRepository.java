package cafegaza.cafegazaspring.repository;

import cafegaza.cafegazaspring.domain.Follow;
import cafegaza.cafegazaspring.domain.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    /*
        팔로우 시, db에 정보 저장
     */
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO Follow(following_id, followed_id) VALUES(:followingId, :followedId)", nativeQuery = true)
    void followById(@Param("followingId") Long followingId, @Param("followedId") Long followedId);

    /*
        팔로우 취소 시, db에 저장된 정보 삭제
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM Follow f WHERE f.followingId= :followingId AND f.followedId= :followedId")
    void unFollowById(@Param("followingId") Member followingId, @Param("followedId") Member followedId);

    /*
        'memberId가 팔로우' 하는 수
     */
    @Query(value = "SELECT COUNT(*) FROM Follow WHERE following_id = :memberId", nativeQuery = true)
    int followingCount(@Param(value = "memberId") Long memberId);

    /*
        'memberId를 팔로우' 하는 수
     */
    @Query(value = "SELECT COUNT(*) FROM Follow WHERE followed_id = :memberId", nativeQuery = true)
    int followedCount(@Param(value = "memberId") Long memberId);

    /*
        memberId가 팔로우 하는 member의 list
     */
    @Query("SELECT f.followedId FROM Follow f WHERE f.followingId = :memberId")
    List<Member> ListFollowingMembers(@Param("memberId") Member memberId);

    /*
        memberId를 팔로우 하는 member의 list
     */
    @Query("SELECT f.followingId FROM Follow f WHERE f.followedId = :memberId")
    List<Member> ListFollowedMembers(@Param("memberId") Member memberId);

    /*
        로그인한 member가 해당 페이지의 사용자를 팔로우했는 지, 안했는 지 확인
        팔로우 했다면 1을, 팔로우하지 않았다면 0을 return
     */
    @Query(value = "SELECT COUNT(*) FROM Follow WHERE following_id = :memberId AND followed_id = :pageMemberId", nativeQuery = true)
    int followState(@Param(value = "memberId") Long memberId, @Param(value = "pageMemberId") Long pageMemberId);

}
