package kr.co.mgv.movie.vo;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieCollect {
    private int movieNo;
    private String targetDt;
    private String title;
    private Date openDt;          // 개봉일
    private int rank;               //해당일자의 박스오피스 순위
    private double rankInten;         //전일대비 순위의 증감분
    private String rankOldAndNew;     // 랭킹에 신규진입여부 "OLD" : 기존 , "NEW" : 신규
    private long scrnCnt;          //해당일자에 상영한 스크린수
    private long showCnt;          //해당일자에 상영된 횟수
    private double salesInten;       //전일 대비 매출액 증감분
    private double salesChange;      //전일 대비 매출액 증감 비율
    private long salesAcc;        //누적매출액
    private long salesAmt;        //
    private long audiCnt;        //해당일의 관객수
    private double audiInten;     //전일 대비 관객수 증감분
    private double audiChange;      // 전일 대비 관객수 증감 비율
    private long audiAcc;        //누적관객수
    private LocalDateTime collectDt;      // 수집일
}
