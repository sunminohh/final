package kr.co.mgv.common.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUtils {

    /**
     * 단일 파일 업로드
     * @param multipartFile - 파일 객체
     * @return DB에 저장할 파일 정보
     */
    public String saveFile(String path, final MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            return null;
        }

        String saveName = generateSaveFilename(multipartFile.getOriginalFilename());

        try {
        	String directory = new ClassPathResource(path).getFile().getAbsolutePath().replace("target\\classes", "src\\main\\resources");
        	File saveFile = new File(directory, saveName);
        	
        	InputStream in = multipartFile.getInputStream();
        	OutputStream out = new FileOutputStream(saveFile);
        	
        	FileCopyUtils.copy(in, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return saveName;
    }

    /**
     * 저장 파일명 생성
     * @param filename 원본 파일명
     * @return 디스크에 저장할 파일명
     */
    private String generateSaveFilename(final String filename) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String extension = StringUtils.getFilenameExtension(filename);
        return uuid + "." + extension;
    }

   
}
