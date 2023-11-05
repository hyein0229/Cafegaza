package cafegaza.cafegazaspring.service;

import cafegaza.cafegazaspring.domain.uploadFile.BbsUploadFile;
import cafegaza.cafegazaspring.domain.uploadFile.UploadFile;
import cafegaza.cafegazaspring.domain.uploadFile.ReviewImage;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class FileHandler {

    /**
        사용자가 업로드한 파일을 파일 엔티티로 변환
     */
    public UploadFile saveFiles(MultipartFile multipartFile, int opt) throws Exception {

        String origFileName = multipartFile.getOriginalFilename(); // 파일 이름 가져오기
        String extension = getExtension(origFileName);
        String filename = null; // 저장될 이름
        try {
            filename = MD5Generator(origFileName); // 파일 이름 문자열을 MD5 로 변환
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 서버에 저장될 위치
        String savedPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images";
        if(!new File(savedPath).exists()){ // files 디렉토리가 존재하지 않으면 생성
            try{
                new File(savedPath).mkdir();
            }catch(Exception e){
                e.getStackTrace();
            }
        }

        // 경로에 파일 저장
        String filePath = savedPath + "\\" + filename + "." + extension;
        multipartFile.transferTo(new File(filePath));

        if (opt == 1) {
            return ReviewImage.builder()
                    .fileOrigName(origFileName)
                    .fileStoredName(filename + "." + extension)
                    .filePath(filePath)
                    .build();
        } else if(opt == 2) {
            return BbsUploadFile.builder()
                    .fileOrigName(origFileName)
                    .fileStoredName(filename + "." + extension)
                    .filePath(filePath)
                    .build();
        }
        return null;
    }

    /**
    문자열을 MD5 체크섬으로 변환하여 서버에 저장되도록 함
    */
    public String MD5Generator(String input) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(input.getBytes(StandardCharsets.UTF_8));
        byte[] md5Hash = md5.digest();
        StringBuilder hexMd5Hash = new StringBuilder();
        for(byte b : md5Hash){
            String hexString = String.format("%02x", b);
            hexMd5Hash.append(hexString);
        }
        return hexMd5Hash.toString();
    }
    /**
        파일 확장자 구해서 반환
     */
    public String getExtension(String fileName) {
        int pos = fileName.lastIndexOf(".");
        String ext = fileName.substring(pos+1);
        return ext;
    }
}
