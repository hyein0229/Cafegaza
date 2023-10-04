package cafegaza.cafegazaspring.controller;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ReviewForm {

    private Long id;

    @NotEmpty
    @Size(max = 800) // 800자까지 제한
    private String content; // 리뷰 내용

    @NotNull
    private int rate; // 별점

    private List<MultipartFile> imageFiles; // 리뷰 이미지 파일


}
