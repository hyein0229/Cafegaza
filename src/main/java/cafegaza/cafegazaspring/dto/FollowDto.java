package cafegaza.cafegazaspring.dto;

import cafegaza.cafegazaspring.domain.Follow;
import cafegaza.cafegazaspring.domain.Member;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FollowDto {
    private Long id;
    private Member followingId;
    private Member followedId;

    public Follow toEntity() {
        return Follow.builder()
                .id(id)
                .followingId(followingId)
                .followedId(followedId)
                .build();
    }

    public static FollowDto toDto(Follow follow) {
        return FollowDto.builder()
                .id(follow.getId())
                .followingId(follow.getFollowingId())
                .followedId(follow.getFollowedId())
                .build();
    }
}
