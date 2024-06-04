package kr.co.moviespring.web.service;

import kr.co.moviespring.web.repository.OnelineReviewLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OnelineReviewLikeServiceImp implements OnelineReviewLikeService {

    @Autowired
    private OnelineReviewLikeRepository repository;

    @Override
    public int like(Long reviewId, Long memberId) {
        repository.save(reviewId, memberId);
        return 1;
    }

    @Override
    public int unlike(Long reviewId, Long memberId) {
        repository.delete(reviewId, memberId);
        return 0;
    }
}
