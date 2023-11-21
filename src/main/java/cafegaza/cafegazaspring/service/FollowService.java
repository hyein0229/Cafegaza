package cafegaza.cafegazaspring.service;

import cafegaza.cafegazaspring.domain.Follow;
import cafegaza.cafegazaspring.domain.Member;
import cafegaza.cafegazaspring.repository.FollowRepository;
import cafegaza.cafegazaspring.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowService {
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public FollowService(FollowRepository followRepository, MemberRepository memberRepository) {
        this.followRepository = followRepository;
        this.memberRepository = memberRepository;
    }

    /*
        팔로우
     */
    public void follow(Long followingId, Long followedId) {
        followRepository.followById(followingId, followedId);
    }

    /*
        팔로우 취소
     */
    public void unFollow(Member followingId, Long followedId) {
        Member followedMember = memberRepository.findById(followedId).orElseThrow();

        followRepository.unFollowById(followingId, followedMember);
    }
}
