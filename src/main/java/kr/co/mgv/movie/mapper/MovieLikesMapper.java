package kr.co.mgv.movie.mapper;

import kr.co.mgv.movie.vo.MovieLikes;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface MovieLikesMapper {
    public void insertMovieLikes(MovieLikes movieLikes);
    public Set<Integer> getLikedMovieNosByUserId(String userId);
    public void deleteMovieLikes(MovieLikes movieLikes);
}
