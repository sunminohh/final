package kr.co.mgv.common;

import kr.co.mgv.board.list.BoardList;
import kr.co.mgv.board.service.MyBoardService;
import kr.co.mgv.common.dao.CommonDao;
import kr.co.mgv.common.vo.MgvFile;
import kr.co.mgv.support.service.NoticeService;
import kr.co.mgv.support.vo.Notice;
import kr.co.mgv.movie.service.MovieService;
import kr.co.mgv.movie.vo.Movie;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

	private final NoticeService noticeService;
    private final MyBoardService myBoardService;
    private final CommonDao commonDao;
    private final MovieService movieService;

    @Value("${default-file-path}")
    private String defaultFilePath;

    @GetMapping("/topMovieImage")
    @ResponseBody
    public String selectTopMovieImageUrl() {
        return commonDao.selectTopMovieImageUrl();
    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal User user, Model model) {
        log.info("[HOME] - User: {}", user != null ? user.getUsername() : "Anonymous");

        List<BoardList> commentList = myBoardService.getBest5("comment");
        List<BoardList> likeList = myBoardService.getBest5("like");
        
        List<Notice> notice = noticeService.getNoticeList();
    	
    	model.addAttribute("notice", notice);
        List<Movie> topMovies = movieService.getMovieChart(4);
        if(user!=null){
            HashSet<Integer> favMovieNos = movieService.getAllLikedMovieNos(user.getId());
            topMovies.forEach(movie -> movie.setLiked(favMovieNos.contains(movie.getNo())));
        }
        model.addAttribute("like", likeList);
        model.addAttribute("comment", commentList);
        model.addAttribute("topMovies",topMovies);
        return "view/index";
    }

    @GetMapping("/common/image/{fileId}")
    public ResponseEntity<UrlResource> downloadFile(@PathVariable Long fileId) {
        MgvFile mgvFile = commonDao.getMgvFile(fileId);
        log.info("[IMAGE]: {}", mgvFile);
        if (mgvFile == null) {
            return noImage();
        }
        try {
            Path filePath = Paths.get(mgvFile.getUploadPath(), File.separator, mgvFile.getStoredName());
            if(!new File(filePath.toUri()).exists()) return noImage();
            UrlResource resource = new UrlResource(filePath.toUri());
            String contentType = Files.probeContentType(filePath);
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
        } catch (IOException e) {
            log.info("[IMAGE]: {}", e.getMessage());
            return noImage();
        }
    }

    public ResponseEntity<UrlResource> noImage() {
        Path filePath = Paths.get(defaultFilePath, File.separator, "no-image.jpg");
        try {
            UrlResource resource = new UrlResource(filePath.toUri());
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("image/jpeg"))
                .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.noContent().build();
        }
    }
}
