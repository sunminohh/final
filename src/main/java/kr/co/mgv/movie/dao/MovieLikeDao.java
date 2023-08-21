package kr.co.mgv.movie.dao;

import kr.co.mgv.movie.vo.MovieLike;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashSet;
import java.util.Set;

@Mapper
public interface MovieLikeDao {
    public void insertMovieLike(MovieLike movieLike);
    public HashSet<Integer> getLikedMovieNosByUserId(String userId);
    public void deleteMovieLike(MovieLike movieLike);
    public Integer isMovieLikedByUser(MovieLike movieLike);
}
