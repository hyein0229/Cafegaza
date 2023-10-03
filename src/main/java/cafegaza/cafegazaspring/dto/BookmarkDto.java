package cafegaza.cafegazaspring.dto;

import cafegaza.cafegazaspring.domain.Bookmark;
import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.domain.Member;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDto {
    private Long id;
    private CafeDto cafe;
    private MemberDto member;

    public Bookmark toEntity() {
        return Bookmark.builder()
                .id(id)
                .cafe(cafe.toEntity())
                .member(member.toEntity())
                .build();
    }

    public static BookmarkDto toDto(Bookmark bookmark) {
        return BookmarkDto.builder()
                .id(bookmark.getId())
                .cafe(CafeDto.toDto(bookmark.getCafe()))
                .member(MemberDto.toDto(bookmark.getMember()))
                .build();
    }
}
