package kr.co.moviespring.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.moviespring.web.entity.OnelineReview;
import kr.co.moviespring.web.repository.CommentRepository;

@Service
public class CommentServiceImp implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    // 한줄평 등록//
    @Override
    public void saveComment(String comments, int rate, Long movieId) {
        commentRepository.save(comments, rate, movieId);
    }

    // 한줄평 목록//
    @Override
    public List<OnelineReview> getOnelineReviews(Long movieId) {
        List<OnelineReview> onelineReviews = commentRepository.getlist(movieId);
        return onelineReviews;
    }
}
