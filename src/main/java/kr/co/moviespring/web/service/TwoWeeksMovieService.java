package kr.co.moviespring.web.service;

import java.util.List;

import kr.co.moviespring.web.entity.Movie;
import kr.co.moviespring.web.entity.TwoWeeksMovie;

public interface TwoWeeksMovieService {

    List<TwoWeeksMovie> findByMovieCd();

    String findGenreName();
    
    //admin 장르별 관객수 순위6위까지 검색
     void findByGenre(String childSelectValue);
     //년도별 6위까지검색
     void findByReleseDate(String childSelectValue);
     //배급사별 6위까지 검색
     void findByDistributor(String childSelectValue);
 
     //관리자 2주영화 등록데이터 조회 비동기(비둘기x)처리
     List<Movie>  findAllEditedList();
    
}