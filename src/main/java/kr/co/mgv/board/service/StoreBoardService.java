package kr.co.mgv.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.mgv.board.BoardPagination;
import kr.co.mgv.board.form.AddSboardForm;
import kr.co.mgv.board.list.StoreBoardList;
import kr.co.mgv.board.mapper.StoreBoardDao;
import kr.co.mgv.board.vo.BoardCategory;
import kr.co.mgv.board.vo.BoardProduct;
import kr.co.mgv.board.vo.SBoardComment;
import kr.co.mgv.board.vo.SBoardLike;
import kr.co.mgv.board.vo.StoreBoard;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreBoardService {

	private final StoreBoardDao storeBoardDao;
	
	public StoreBoardList getSBoards(Map<String, Object> param) {
		
		// pagination
		int totalRows = storeBoardDao.getTotalRows(param);
		
		int page = (int) param.get("page");
		int rows = (int) param.get("rows");
		BoardPagination pagination = new BoardPagination(rows, page, totalRows);
		int begin = pagination.getBegin();
		int end = pagination.getEnd();
		
		param.put("begin", begin);
		param.put("end", end);
		
		// 게시판 목록
		List<StoreBoard> storeBoards = storeBoardDao.getSBoards(param);
		
		// 카테고리 목록
		List<BoardCategory> categories = storeBoardDao.getCatetories();
		
		// 조회한 목록을 담을 list를 생성한다.
		StoreBoardList result = new StoreBoardList();
		
		// TheaterBoardList에 pagination과 게시판 목록, 카테고리 목록을 담는다.
		result.setCategories(categories);
		result.setPagination(pagination);
		result.setStoreBoards(storeBoards);
		
		return result;
	}
	
	public List<BoardCategory> getCategories(){
		return storeBoardDao.getCatetories();
	}
	
	public List<BoardProduct> getProductsByCatNo(int catNo){
		List<BoardProduct> products = storeBoardDao.getProductsByCatNo(catNo);
		return products;
	}
	
	// 상세페이지
	public void increseRead(int no) {
		StoreBoard board = storeBoardDao.getSBoardByNo(no);
		board.setReadCount(board.getReadCount()+1);
		storeBoardDao.updateSBoardByNo(board);
	}
	
	public StoreBoard getStoreBoardByNo (int no) {
		return storeBoardDao.getSBoardByNo(no);
	}
	
	public void updateBoardLike(int no, int likeCount) {
		StoreBoard board = storeBoardDao.getSBoardByNo(no);
		board.setLikeCount(likeCount);
		storeBoardDao.updateSBoardByNo(board);
	}
	
	public void insertBoardLike (SBoardLike like) {
		storeBoardDao.insertSBoardLike(like);
	}
	
	public SBoardLike getLike(SBoardLike like) {
		return storeBoardDao.getLikeByBnoAndId(like);
	}
	
	public void updateSboardLike(SBoardLike like) {
		storeBoardDao.updateLike(like);
	}

	// 댓글관련
	public void SBoardCommentInsert(SBoardComment comment) {
		storeBoardDao.insertSBoardComment(comment);
	}
	
	public void updateBoardComment(int no, int commentCount) {
		StoreBoard board = storeBoardDao.getSBoardByNo(no);
		board.setCommentCount(commentCount);
		storeBoardDao.updateSBoardByNo(board);
	}
	
	public List<SBoardComment> getComments(int no) {
		return storeBoardDao.getSBoardComments(no);
	}
	
	public List<SBoardComment> getChildComments(int no)	{
		return storeBoardDao.getSBoardChildComments(no);
	}
	
	public SBoardComment getGreatComment(int no, String id) {
		User user = User.builder().id(id).build();
		StoreBoard board = StoreBoard.builder().no(no).build();
		SBoardComment comment = SBoardComment.builder().user(user).board(board).build();
		return storeBoardDao.getGreatComment(comment);
	}
	
	public SBoardComment getChildComment(int no, String id) {
		User user = User.builder().id(id).build();
		StoreBoard board = StoreBoard.builder().no(no).build();
		SBoardComment comment = SBoardComment.builder().user(user).board(board).build();
		return storeBoardDao.getChildComment(comment);
	}
	
	public void greatCommentDelete (int no) {
		storeBoardDao.deleteGreatComment(no);
	}
	
	public void childCommentDelete (int no) {
		storeBoardDao.deleteChildsComment(no);
	}
	
	public int getTotalChildCount (int no) {
		return storeBoardDao.getTotalCommentCount(no);
	}
	
	// 게시물 CRUD
	public void addSboard(AddSboardForm form, User user) {
		try {
			BoardCategory category = BoardCategory.builder().no(form.getCatNo()).build();
			BoardProduct product = BoardProduct.builder().no(form.getProductNo()).build();
			StoreBoard board = StoreBoard.builder()
								.user(user)
								.category(category)
								.product(product)
								.name(form.getName())
								.content(form.getContent())
								.build();
			storeBoardDao.insertSboard(board);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
