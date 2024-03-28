package kr.co.moviespring.web.movieapi;

public class DailyBoxEntity {
    private int rank;       //해당일자의 박스오피스 순위
    private String movieCd;   //영화의 대표코드
    private long audiCnt;   //해당일의 관객수
    private long audiAcc;   //누적관객수
    private String salesAcc;   //누적매출액


    public int getRank() {
        return rank;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }
    public String getMovieCd() {
        return movieCd;
    }
    public void setMovieCd(String movieCd) {
        this.movieCd = movieCd;
    }
    public long getAudiCnt() {
        return audiCnt;
    }
    public void setAudiCnt(long audiCnt) {
        this.audiCnt = audiCnt;
    }
    public long getAudiAcc() {
        return audiAcc;
    }
    public void setAudiAcc(long audiAcc) {
        this.audiAcc = audiAcc;
    }
    public String getSalesAcc() {
        return salesAcc;
    }
    public void setSalesAcc(String salesAcc) {
        this.salesAcc = salesAcc;
    }
}
