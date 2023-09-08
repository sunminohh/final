package kr.co.mgv.common;

import kr.co.mgv.board.list.BoardList;
import kr.co.mgv.board.service.MyBoardService;
import kr.co.mgv.common.dao.CommonDao;
import kr.co.mgv.common.vo.MgvFile;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

import javax.annotation.Resource;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
	
	private final MyBoardService myBoardService;
	private final CommonDao commonDao;

	@GetMapping("/")
	public String home(@AuthenticationPrincipal User user, Model model) {
		log.info("[HOME] - User: {}", user != null ? user.getUsername() : "Anonymous");
		
		List<BoardList> commentList = myBoardService.getBest5("comment");
		List<BoardList> likeList = myBoardService.getBest5("like");
		
		model.addAttribute("like", likeList);
		model.addAttribute("comment", commentList);
		return "view/index";
	}

	@GetMapping("/common/image/{fileId}")
	public ResponseEntity<UrlResource> downloadFile(@PathVariable Long fileId) {
		MgvFile mgvFile = commonDao.getMgvFile(fileId);
		if (mgvFile == null) {
			return ResponseEntity.badRequest().build();
		}

		Path filePath = Paths.get(mgvFile.getUploadPath(), File.separator, mgvFile.getStoredName());
		UrlResource resource;
		try {
			resource = new UrlResource(filePath.toUri());
		} catch (MalformedURLException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		String contentType;
		try {
			contentType = Files.probeContentType(filePath);
		} catch (IOException e) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType(contentType))
			.body(resource);
	}
}
