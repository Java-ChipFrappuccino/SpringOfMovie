package kr.co.moviespring.web.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.moviespring.web.entity.Movie;

@Mapper
public interface MovieRepository {
    
    void save(Movie movie);
    List <Movie> findAll();

    Movie findById(Long id);
}
