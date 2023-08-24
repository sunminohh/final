package kr.co.mgv.support.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.co.mgv.common.file.FileUtils;
import kr.co.mgv.support.dao.LostDao;
import kr.co.mgv.support.dao.OneDao;
import kr.co.mgv.support.dto.OneList;
import kr.co.mgv.support.form.AddOneForm;
import kr.co.mgv.support.vo.One;
import kr.co.mgv.support.vo.OneFile;
import kr.co.mgv.support.vo.SupportCategory;
import kr.co.mgv.support.vo.SupportPagination;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OneService {
	
	private final OneDao oneDao;
	private final FileUtils fileUtils;
	
	public void insertOne(AddOneForm form, User user) {
		
		Location location = null;
		if (form.getLocationNo() != null) {
				location = Location.builder()
					.no(form.getLocationNo())
					.build();
	    }
		
		Theater theater = null;
	    if (form.getTheaterNo() != null) {
	    	  	theater = Theater.builder()
						.no(form.getTheaterNo())
						.build();
	    }

	    SupportCategory category = SupportCategory.builder()
							.no(form.getCategoryNo())
							.build();
		
		One one = null;
		if (user != null) {
			one = One.builder()
					.user(user)
					.location(location)
					.theater(theater)
					.category(category)
					.title(form.getTitle())
					.content(form.getContent())
					.build();
		} else {
			one = One.builder()
					.guestName(form.getGuestName())
					.guestEmail(form.getGuestEmail())
					.guestPassword(form.getGuestPassword())
					.location(location)
					.theater(theater)
					.category(category)
					.title(form.getTitle())
					.content(form.getContent())
					.build();
		}
		
		oneDao.insertOne(one);
		
		List<MultipartFile> multipartFiles = form.getFiles();
		for (MultipartFile multipartFile : multipartFiles) {
			String originalFilename = multipartFile.getOriginalFilename();
			String saveFilename = fileUtils.saveFile("static/images/support/one", multipartFile);
			
			OneFile oneFile = new OneFile();
			oneFile.setOne(one);
			oneFile.setOriginalName(originalFilename);
			oneFile.setSaveName(saveFilename);
			
			oneDao.insertOneFile(oneFile);
		}
	}
	
	public void deleteOne(int no) {
		One one = oneDao.getOneByNo(no);
		one.setDeleted("Y");
		
		oneDao.updateOneByNo(one);
	}
	
	public OneList search(Map<String, Object> param) {
		int totalRows = oneDao.getTotalRows(param);
		int page = (int) param.get("page");
		
		SupportPagination pagination = new SupportPagination(page, totalRows);
		
		int begin = pagination.getBegin();
		int end = pagination.getEnd();
		
		param.put("begin", begin);
		param.put("end", end);
		List<One> oneList = oneDao.getOnes(param);
		OneList result = new OneList();
		
		result.setPagination(pagination);
		result.setOneList(oneList);
	
		return result;
	}
	
	public One getOneByNo(int oneNo) {
		return oneDao.getOneByNo(oneNo);
	}
	
	public List<OneFile> getOneFileByOneNo(int oneNo) {
		return oneDao.getOneFileByOneNo(oneNo);
	}
	
	public OneFile getOneFileByFileNo(int fileNo) {
		return oneDao.getOneFileByFileNo(fileNo);
	}

	public List<SupportCategory> getCategoriesByType(String categoryType) {
		
		return oneDao.getCategories(categoryType);
	}
	
	public List<Location> getLocations() {
		
		return oneDao.getLocations();
	}
	
	public List<Theater> getTheatesrByLocationNo(int locationNo) {
		
		return oneDao.getTheatersByLocationNo(locationNo);
	}

}










