package kr.co.mgv.crawler;

import kr.co.mgv.movie.batch.MovieScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MovieSchedulerTests {

    @Autowired
    MovieScheduler movieScheduler;

    @Test
    void daily() throws Exception {
        movieScheduler.collectYesterdayBoxOffice();
    }

}
