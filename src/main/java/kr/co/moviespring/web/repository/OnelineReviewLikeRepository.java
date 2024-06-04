package kr.co.moviespring.web.repository;

import kr.co.moviespring.web.entity.OnelineReview;
import kr.co.moviespring.web.entity.OnelineReviewMovieView;
import kr.co.moviespring.web.entity.OnelineReviewView;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OnelineReviewLikeRepository {

    // 한줄평 좋아요
    void save(Long reviewId, Long memberId);

    // 한줄평 좋아요 취소
    void delete(Long reviewId, Long memberId);
}
