package kr.co.moviespring.web.movieapi;

import java.util.Date;

import com.google.gson.JsonArray;

//영화 상세 정보
public class MovieInfoEntity {
    private String movieCd; //영화상세정보(영화코드) > movieCd
    private String movieNm; //영화상세정보(한글이름) > movieNm
    private String movieNmEn; //영화상세정보(영문) > movieNmEn
    private String openDt; //영화상세정보(개봉일자) > openDt
    private String nationNm;//영화상세정보(제작국가) > nations/nationNm
    private String genreNm;//영화상세정보(장르) > genres/genreNm

    
    public String getMovieCd() {
        return movieCd;
    }
    public void setMovieCd(String movieCd) {
        this.movieCd = movieCd;
    }
    public String getMovieNm() {
        return movieNm;
    }
    public void setMovieNm(String movieNm) {
        this.movieNm = movieNm;
    }
    public String getMovieNmEn() {
        return movieNmEn;
    }
    public void setMovieNmEn(String movieNmEn) {
        this.movieNmEn = movieNmEn;
    }
    public String getOpenDt() {
        return openDt;
    }
    public void setOpenDt(String openDt) {
        this.openDt = openDt;
    }
    public String getNationNm() {
        return nationNm;
    }
    public void setNationNm(String nationNm) {
        this.nationNm = nationNm;
    }
    public String getGenreNm() {
        return genreNm;
    }
    public void setGenreNm(String genreNm) {
        this.genreNm = genreNm;
    }
    public JsonArray getJSONArray(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getJSONArray'");
    }
}


