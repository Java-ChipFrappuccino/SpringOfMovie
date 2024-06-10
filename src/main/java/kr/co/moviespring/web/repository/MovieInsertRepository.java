package kr.co.moviespring.web.repository;

import kr.co.moviespring.web.entity.Movie2;
import kr.co.moviespring.web.entity.Movie3;
import kr.co.moviespring.web.entity.MovieSearchView;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MovieInsertRepository {

    //일일 박스 오피스 가져오는것
    void DailyBoxOfficeSave(Movie2 movie2);

    //쓸일 없을거같음.
//    void saveAll(Movie2 movie2);

    //영화가 없으면 등록함.
    void saveIfNotMovie(Movie2 movie2);

    //엑셀에서 가져온 누적관객수 데이터 넣기.
    void excelDataInsert(Movie2 movie2);

    //영화 데이터 업데이트
    void MovieDataUpdate(Movie2 movie2);

    // 태평 Movie3 가져오기 테스트
    List<Movie3> getlist(String year);

    List<Movie3> getlistByPrd(String year);

    Movie3 findByCode(String code);

    void save(Movie3 movie);

    int getCount(String query);

    List<Movie3> findByQuery(String query, int offset, int size);

}
