package kr.co.moviespring.web.service;

import java.util.List;

import kr.co.moviespring.web.entity.Movie;

public interface MovieService {
    List <Movie> getList();
    List <Movie> getByQuery(String query);
    Movie getById(Long id);
    Movie getByTMDBId(String id);
    Long saveMovie(Movie movie);
}
