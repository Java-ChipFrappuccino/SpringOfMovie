package kr.co.moviespring.web.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.moviespring.web.entity.OnelineReview;
import kr.co.moviespring.web.entity.OnelineReviewMovieView;
import kr.co.moviespring.web.entity.OnelineReviewView;

@Mapper
public interface OnelineReviewRepository {
    // 한줄평 등록
    void save(Long memberId, String comments, int rate, Long movieId);

    // 한줄평 수정
    void update(Long memberId, String comments, int rate, Long movieId);

    // 한줄평 목록
    List<OnelineReviewView> findAll(Long movieId);
    List<OnelineReviewView> findAllByCategory(Long movieId, Long memberId, String category);

    List<OnelineReviewMovieView> findAllByMemberId(Long id);

    // 회원이 작성한 리뷰 가져오기
    OnelineReview findById(Long movieId, Long memberId);

    int getCount(Long memberId);

    void deleteCommentById(Long memberId, Long movieId);
}
