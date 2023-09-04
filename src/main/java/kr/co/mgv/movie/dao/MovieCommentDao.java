package kr.co.mgv.movie.dao;

import kr.co.mgv.movie.dto.MovieCommentDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MovieCommentDao {
    public void insertMovieComment(MovieCommentDto dto);
}
