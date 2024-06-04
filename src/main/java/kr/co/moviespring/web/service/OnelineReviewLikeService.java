package kr.co.moviespring.web.service;

public interface OnelineReviewLikeService {
    //한줄평 좋아요
    int like(Long reviewId, Long memberId);
    //한줄평 좋아요 취소
    int unlike(Long reviewId, Long memberId);
}
