package kr.co.moviespring.web.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    private Long id; //영화상세정보(영화코드) > movieCd
    private String korName; //영화상세정보(한글이름) > movieNm
    private String engName; //영화상세정보(영문) > movieNmEn
    private String sponsor; //일단 null
    private Date releaseDate; //영화상세정보(개봉일자) > openDt
    private int totalRate;//일단 null
    private int totalBoxoffice;//영화상세정보(랭킹) > audiAcc
    private int totalSales;//영화상세정보(누적매출액) > salesAcc
    private int dailySales;//일단 null
    private int totalAudience;//영화상세정보(누적관객수) > audiAcc
    private int dailyAudience;//영화상세정보(하루관객수)
    private String movieIntro;//영화 상세 검색(MOVIES>Details)(소개글) tmdb overview
    private String posterUrl;//영화 상세 검색(MOVIES>Details) (포스트) tmdb poster_path
    private String stillcutUrl;//이건(스틸컷이미지) tmdb
    private String trailerUrl;//이건(트레일러영상) tmdb
    private Long releaseNationId;//영화상세정보(제작국가) > nations/nationNm
    private Long genreId;//영화상세정보(장르) > genres/genreNm
    private String showTime;//영화상세정보(상영시간) > showTm":"131"
    private String watchGrade;//"audits":[{"auditNo":"2012-F610","watchGradeNm":"15세이상관람가"}]"
}
