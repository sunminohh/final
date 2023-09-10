package kr.co.mgv.common.vo;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MgvFile {
    private long fileId;
    private String originName;
    private String storedName;
    private String fileExtension;
    private long fileSize;
    private String uploadPath;
    private LocalDateTime regDate;
}
