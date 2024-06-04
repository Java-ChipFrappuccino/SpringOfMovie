package kr.co.moviespring.web.controller.api;

import kr.co.moviespring.web.config.security.CustomUserDetails;
import kr.co.moviespring.web.entity.OnelineReviewView;
import kr.co.moviespring.web.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController("ApiOnelineReviewController")
@RequestMapping("api/oneline-reviews")
public class OnelineReviewController {

    @Autowired
    private OnelineReviewService service;

    @Autowired
    private OnelineReviewLikeService likeService;


    // 카테고리 요청
    @GetMapping("{movieId}/{category}")
    public List<OnelineReviewView> list(@PathVariable String category,
                                        @PathVariable Long movieId,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = null;
        if (userDetails != null) {
            memberId = userDetails.getId();
        }

        List<OnelineReviewView> list = service.getListByCategory(movieId, memberId, category);
        return list;
    }

    // 좋아요 요청
    @PostMapping("{reviewId}/like")
    public int like(@PathVariable Long reviewId,
                    @AuthenticationPrincipal CustomUserDetails userDetails) {
//        @RequestBody LikeRequest LikeRequest
        if (userDetails == null)
            return 100; //로그인하지 않은 유저가 좋아요 누를시 100 전송

        Long memberId = userDetails.getId();
        int result = likeService.like(reviewId, memberId);

        return result;
    }

    // 좋아요 삭제 요청
    @DeleteMapping("{reviewId}/unlike")
    public int delete(@PathVariable Long reviewId,
                      @AuthenticationPrincipal CustomUserDetails userDetails) {
//        @RequestBody LikeRequest LikeRequest
        if (userDetails == null)
            return 100; //로그인하지 않은 유저가 좋아요 누를시 100 전송

        Long memberId = userDetails.getId();
        int result = likeService.unlike(reviewId, memberId);

        return result;
    }

}