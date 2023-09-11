package kr.co.mgv.movie.dao;

import kr.co.mgv.movie.vo.MovieComment;
import kr.co.mgv.movie.vo.MovieCommentLike;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashSet;
import java.util.List;

@Mapper
public interface MovieCommentDao {
    public void insertMovieComment(MovieComment movieComment);
    public List<MovieComment> getMovieCommentsByMovieNo(int movieNo);
    public MovieComment getMovieCommentByCommentNo(long no);
    public void incrementMovieCommentLike(long movieCommentNo);
    public void decrementMovieCommentLike(long movieCommentNo);
    public void insertMovieCommentLike(MovieCommentLike movieCommentLike);
    public void deleteMovieCommentLike(MovieCommentLike movieCommentLike);
    public void deleteMovieCommentByNo(long no);
    public void deleteMovieCommentLikeByCommentNo(long no);
    public HashSet<Long> getMovieCommentLikeByUserId(String userId);
}
