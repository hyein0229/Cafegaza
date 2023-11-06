package cafegaza.cafegazaspring.service;

import cafegaza.cafegazaspring.domain.uploadFile.File;
import cafegaza.cafegazaspring.domain.uploadFile.ReviewImage;
import cafegaza.cafegazaspring.dto.FileDto;
import cafegaza.cafegazaspring.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileHandler fileHandler;
    private final FileRepository fileRepository;

    public FileDto saveReviewImage(MultipartFile multipartFile) throws Exception {

        // 파일 로컬에 저장 후 저장 정보 반환
        Map<String, String> fileInfo = fileHandler.saveFiles(multipartFile);
        // 엔티티 생성
        ReviewImage reviewImage = ReviewImage.builder()
                        .fileOrigName(fileInfo.get("fileOrigName"))
                        .fileStoredName(fileInfo.get("fileStoredName"))
                        .filePath(fileInfo.get("filePath"))
                        .build();

        fileRepository.save(reviewImage);
        return FileDto.toDto(reviewImage);
    }
}
