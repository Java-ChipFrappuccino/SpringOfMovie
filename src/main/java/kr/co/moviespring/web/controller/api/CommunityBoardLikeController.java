package kr.co.moviespring.web.controller.api;

import kr.co.moviespring.web.config.security.CustomUserDetails;
import kr.co.moviespring.web.service.CommunityBoardLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController("ApiCommunityBoardLikeController")
@RequestMapping("api/community-board-likes")
public class CommunityBoardLikeController {

    @Autowired
    private CommunityBoardLikeService service;

    @PostMapping("{boardId}/like")
    public int like(@PathVariable Long boardId,
                    @AuthenticationPrincipal CustomUserDetails userDetails) {
//        @RequestBody LikeRequest LikeRequest
        if (userDetails == null)
            return 100; //로그인하지 않은 유저가 좋아요 누를시 100 전송

        Long userId = userDetails.getId();
        int status = service.like(boardId, userId, 1); //좋아요는 status값으로 1을 전송하고 1을 리턴 받음

        return status;
    }

    @PostMapping("{boardId}/dislike")
    public int disLike(@PathVariable Long boardId,
                       @AuthenticationPrincipal CustomUserDetails userDetails) {
//        @RequestBody LikeRequest LikeRequest
        if (userDetails == null)
            return 100; //로그인하지 않은 유저가 좋아요 누를시 100 전송

        Long userId = userDetails.getId();
        int status = service.disLike(boardId, userId, -1); //싫어요는 status값으로 -1을 전송하고 -1을 리턴 받음

        return status;
    }

}