package kr.co.mgv.movie.mapper;

import kr.co.mgv.movie.vo.Movie;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashSet;
import java.util.List;

@Mapper
public interface MovieMapper {
    void insertMovie(Movie movie);
    HashSet<Integer> getMovieNos();
    List<Movie> getAllMovies();
    List<Movie> getMoviesByRowNum(int rowNum);
    void updateMovie(Movie movie);
    void initChart(int movieNo);
    void syncMovie(Movie movie);

}
