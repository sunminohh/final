package kr.co.mgv.common.file;

import kr.co.mgv.common.vo.MgvFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUtils {

    @Value("${default-file-path}")
    private String defaultFilePath;

    /**
     * 단일 파일 업로드
     * @param file - 파일 객체
     * @return DB에 저장할 파일 정보
     */
    public MgvFile saveFile(String subPath, final MultipartFile file) {

        if (file.isEmpty()) {
            return null;
        }
        String originalFileName = file.getOriginalFilename();
        String fullPath = makeDir(defaultFilePath + File.separator + subPath);
        String saveName = generateSaveFilename(file.getOriginalFilename());
        File saveFile = new File(fullPath, saveName);

        try {
            file.transferTo(saveFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return MgvFile.builder()
            .fileSize(file.getSize())
            .originName(originalFileName)
            .fileExtension(file.getContentType())
            .storedName(saveName)
            .uploadPath(fullPath)
            .build();
    }

    /**
     * 이미지 파일 삭제
     * @param path 이미지 파일 경로
     * @param filename 이미지 파일 이름
     * @return DB에 저장한 파일 이름
     */
    public boolean deleteFile(String path, String filename) {

        if (filename == null || filename.isEmpty()) {
            return false;
        }

        File fileToDelete = new File(path, filename);
        if (fileToDelete.exists()) {
            return fileToDelete.delete();
        }

        return false;
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

    private String makeDir(String uploadPath) {
        String path = (uploadPath + LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("/yyyy/MM/dd/")))
            .replace("/", File.separator);
        File uploadDir = new File(path);
        if (!uploadDir.exists()) {
            log.info("[UPLOAD_FILE] : {} - {}", path, uploadDir.mkdirs() ? "디렉토리 생성" : "디렉토리 존재");
        }
        return path;
    }

}
