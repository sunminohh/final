package kr.co.mgv.support.service;


import java.util.List;
import java.util.Map;

import kr.co.mgv.common.vo.MgvFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import kr.co.mgv.common.file.FileUtils;
import kr.co.mgv.support.dao.LostDao;
import kr.co.mgv.support.dto.LostList;
import kr.co.mgv.support.form.AddLostForm;
import kr.co.mgv.support.vo.Lost;
import kr.co.mgv.support.vo.LostComment;
import kr.co.mgv.support.vo.LostFile;
import kr.co.mgv.support.vo.SupportPagination;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;
import kr.co.mgv.user.service.EmailService;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LostService {

	private final EmailService emailService;
	private final LostDao lostDao;
	private final FileUtils fileUtils;
	
	private final static String LOST_IMAGE_DIRECTORY = "lost";
	
	public List<LostComment> getLostCommentsByLost(int lostNo) {
		return lostDao.getLostCommentsByLost(lostNo);
	}
	
	public void insertComment(User user, int lostNo, String content) throws Exception  {
		Lost lost = lostDao.getLostByNo(lostNo);
		
		LostComment comment =LostComment.builder().
							user(user).
							lost(lost).
							content(content).build();
		
		lostDao.insertComment(comment);
		
		lost.setAnswered("Y");
		lostDao.updateLostByNo(lost);
		
		String email = null;
		if (lost.getUser() != null) {
			email = lost.getUser().getEmail();
		} else {
			email = lost.getGuestEmail();
		}
		
		emailService.sendTempqnaMessage(email, lost.getHtmlContent(), comment.getHtmlContent());
	}
	
	public void deleteComment(int commentNo) {
		LostComment lostComment = lostDao.getLostCommentByNo(commentNo);
		lostComment.setDeleted("Y");
		
		lostDao.deleteComment(lostComment);
	}
	
	public void insertLost(AddLostForm form, User user) {
		
		Location location = Location.builder()
							.no(form.getLocationNo())
							.build();
		
		Theater theater = Theater.builder()
							.no(form.getTheaterNo())
							.build();
		
		Lost lost = null;
		if (user != null) {
			lost = Lost.builder()
					.user(user) // If user is not null
					.location(location)
					.theater(theater)
					.title(form.getTitle())
					.content(form.getContent())
					.build();
			
		} else {
			lost = Lost.builder()
					.guestName(form.getGuestName()) // If user is null
					.guestEmail(form.getGuestEmail()) // If user is null
					.guestPassword(form.getGuestPassword()) // If user is null
					.location(location)
					.theater(theater)
					.title(form.getTitle())
					.content(form.getContent())
					.build();
		}
		
		lostDao.insertLost(lost);
		
		List<MultipartFile> multipartFiles = form.getFiles();
		for (MultipartFile multipartFile : multipartFiles) {
			String originalFilename = multipartFile.getOriginalFilename();
			if (StringUtils.hasText(originalFilename)) {
				MgvFile saveFile = fileUtils.saveFile(LOST_IMAGE_DIRECTORY, multipartFile);
				LostFile lostFile = new LostFile();
				lostFile.setLost(lost);
				lostFile.setOriginalName(originalFilename);
				lostFile.setSaveName(saveFile.getStoredName());
				lostFile.setUploadPath(saveFile.getUploadPath());
				lostDao.insertLostFile(lostFile);
			}
		}
	}
	
	public void deleteLost(int no) {
		Lost lost = lostDao.getLostByNo(no);
		lost.setDeleted("Y");
		
		lostDao.updateLostByNo(lost);
	}
	

	public LostList search(Map<String, Object> param) {
		int totalRows = lostDao.getTotalRows(param);
		int page = (int) param.get("page");
		
		SupportPagination pagination = new SupportPagination(page, totalRows);
		
		int begin = pagination.getBegin();
		int end = pagination.getEnd();
		
		param.put("begin", begin);
		param.put("end", end);
		List<Lost> lostList = lostDao.getlosts(param);
		LostList result = new LostList();
		
		result.setPagination(pagination);;
		result.setLostList(lostList);
		
		return result;
	}
	
	public Lost getLostByNo(int lostNo) {
		return lostDao.getLostByNo(lostNo);
	}
	
	public List<LostFile> getLostFilesByLostNo(int lostNo) {
		return lostDao.getLostFilesByLostNo(lostNo);
	}
	
	public LostFile getLostFileByFileNo(int fileNo) {
		return lostDao.getLostFileByFileNo(fileNo);
	}
	
	public List<Location> getLocations() {
		return lostDao.getLocations();
	}
	
	public List<Theater> getTheatesrByLocationNo(int locationNo) {
		return lostDao.getTheatersByLocationNo(locationNo);
	}
}

















