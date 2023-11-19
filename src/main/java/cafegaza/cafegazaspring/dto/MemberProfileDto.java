package cafegaza.cafegazaspring.dto;

import cafegaza.cafegazaspring.domain.Member;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class MemberProfileDto {
    private Member member;

    private int followState;     // 팔로우/언팔로우 상태
    private int followingCount;  // 팔로잉 수
    private int followedCount;   // 팔로워 수

    private List<Member> followingMemberList = new ArrayList<>();  // 팔로잉 member list
    private List<Member> followedMemberList = new ArrayList<>();   // 팔로워 member list
}
