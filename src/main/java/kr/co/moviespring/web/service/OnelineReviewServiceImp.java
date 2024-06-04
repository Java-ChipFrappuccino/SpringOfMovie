package kr.co.moviespring.web.service;

import java.util.List;

import kr.co.moviespring.web.entity.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.moviespring.web.entity.OnelineReview;
import kr.co.moviespring.web.entity.OnelineReviewMovieView;
import kr.co.moviespring.web.entity.OnelineReviewView;
import kr.co.moviespring.web.repository.OnelineReviewRepository;
import kr.co.moviespring.web.repository.MemberRepository;

@Service
public class OnelineReviewServiceImp implements OnelineReviewService {

    @Autowired
    OnelineReviewRepository onelineReviewRepository;

    @Autowired
    MemberRepository memberRepository;

    // 한줄평 등록
    @Override
    public void saveComment(Long memberId, String comments, int rate, Long movieId) {
//        Member member = memberRepository.findByMembername(id); // 필요없어짐, 혹시모르니 임시보관
        onelineReviewRepository.save(memberId, comments, rate, movieId);
    }

    // 한줄평 수정
    @Override
    public void editComment(Long memberId, String comments, int rate, Long movieId) {
        onelineReviewRepository.update(memberId, comments, rate, movieId);
    }

    // 한줄평 목록//
    @Override
    public List<OnelineReviewView> getList(Long movieId) {
        List<OnelineReviewView> onelineReviews = onelineReviewRepository.findAll(movieId);
        return onelineReviews;
    }

    @Override
    public List<OnelineReviewView> getListByCategory(Long movieId, Long memberId, String category) {
        List<OnelineReviewView> onelineReviews = onelineReviewRepository.findAllByCategory(movieId,memberId,category);
        return onelineReviews;
    }

    @Override
    public List<OnelineReviewMovieView> getListByMemberId(Long id) {
        return onelineReviewRepository.findAllByMemberId(id);
    }

    // 회원이 작성한 리뷰 가져오기
    @Override
    public OnelineReview getById(Long movieId, Long memberId) {
        OnelineReview review = onelineReviewRepository.findById(movieId, memberId);
        return review;
    }

    @Override
    public int getCount(Long memberId) {
        int cnt = onelineReviewRepository.getCount(memberId);
        return cnt;
    }

    @Override
    public void deleteComment(Long memberId, Long movieId) {
        onelineReviewRepository.deleteCommentById(memberId,movieId);
    }
}
