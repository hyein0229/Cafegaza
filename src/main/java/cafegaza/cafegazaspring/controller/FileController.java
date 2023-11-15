package cafegaza.cafegazaspring.controller;

import cafegaza.cafegazaspring.dto.FileDto;
import cafegaza.cafegazaspring.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * 리뷰 이미지를 등록하였을 때 등록 요청 처리
     */
    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        FileDto file  = fileService.saveReviewImage(multipartFile);
        return new ResponseEntity<>(file, HttpStatus.OK);

    }
}
