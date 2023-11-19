package cafegaza.cafegazaspring.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class FileHandler {

    /**
        사용자가 업로드한 파일을 파일 엔티티로 변환
     */
    public Map<String, String> saveFiles(MultipartFile multipartFile) throws Exception {

        Map<String, String> fileInfo = new HashMap<>(); // 파일 저장 후 저장 정보를 반환할 Map
        String origFileName = multipartFile.getOriginalFilename(); // 파일 이름 가져오기
        String filename = UUID.randomUUID() + "." + getExtension(origFileName); // 실제 저장될 이름

        String savedPath = getSavedPath("images"); // 저장될 디렉토리 위치
        String filePath = savedPath + "\\" + filename; // 파일 경로
        multipartFile.transferTo(new java.io.File(filePath)); // 경로에 파일 저장
        // 저장 정보 반환
        fileInfo.put("fileOrigName", origFileName);
        fileInfo.put("fileStoredName", filename);
        fileInfo.put("filePath", filePath);
        return fileInfo;
    }

    // 파일 확장자 반환
    private String getExtension(String fileName) {
        int pos = fileName.lastIndexOf(".");
        String ext = fileName.substring(pos+1);
        return ext;
    }

    // 서버에 저장될 위치(디렉토리) 생성 및 가져오기
    private String getSavedPath(String directory) {

        String savedPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\" + directory;
        if(!new java.io.File(savedPath).exists()){ // 디렉토리가 존재하지 않으면 생성
            try{
                new java.io.File(savedPath).mkdir();
            }catch(Exception e){
                e.getStackTrace();
            }
        }
        return savedPath;
    }
}
