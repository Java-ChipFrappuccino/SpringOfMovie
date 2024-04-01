package kr.co.moviespring.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.moviespring.web.entity.Movie;
import kr.co.moviespring.web.repository.MovieRepository;

@Service
public class MovieServiceImp implements MovieService {

    // 모든 목록 가져오기//
    @Autowired
    MovieRepository repository;

    @Override
    public List<Movie> getList() {

        List<Movie> list = repository.findAll();
        return list;
    }

    // 요청 id값으로 상세정보가져오기//
    @Override
    public Movie getById(Long id) {

        Movie movie = repository.findById(id);
        return movie;
    }

    // ?? //
    @Override
    public void saveMovie(Movie movie) {
        repository.save(movie);
    }

}