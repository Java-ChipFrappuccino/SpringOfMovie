package kr.co.moviespring.web.service;

import java.util.List;

import kr.co.moviespring.web.entity.OnelineReview;
import kr.co.moviespring.web.entity.OnelineReviewMovieView;
import kr.co.moviespring.web.entity.OnelineReviewView;

public interface OnelineReviewService {

    // 한줄평 등록
    void saveComment(Long memberId, String comments ,int rate, Long movieId);
    //한줄평 수정
    void editComment(Long memberId, String comments, int rate, Long movieId);
    List<OnelineReviewView> getList(Long movieId);
    List<OnelineReviewView> getListByCategory(Long movieId, Long memberId, String category);
    List<OnelineReviewMovieView> getListByMemberId(Long id);
    // 회원이 작성한 리뷰 가져오기
    OnelineReview getById(Long movieId, Long memberId);
    int getCount(Long memberId);
    void deleteComment(Long memberId, Long movieId);

}
