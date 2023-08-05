package kr.co.mgv.movie.mapper;

import kr.co.mgv.movie.vo.Movie;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashSet;
import java.util.List;

@Mapper
public interface MovieMapper {
    public Movie getMovieByMovieNo(int movieNo);
    public void insertMovie(Movie movie);
    public HashSet<Integer> getMovieNos();
    public List<Movie> getAllMovies();
    public List<Movie> getMoviesByRowNum(int rowNum);
    public void updateMovie(Movie movie);
    public void initChart(int movieNo);
    public void syncMovie(Movie movie);

}
