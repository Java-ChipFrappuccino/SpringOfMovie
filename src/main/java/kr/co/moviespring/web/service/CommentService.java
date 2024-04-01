package kr.co.moviespring.web.service;

import java.util.List;

import kr.co.moviespring.web.entity.OnelineReview;

public interface CommentService {

    void saveComment(String comments ,int rate, Long movieId);

    List<OnelineReview> getOnelineReviews(Long movieId);
    
}
