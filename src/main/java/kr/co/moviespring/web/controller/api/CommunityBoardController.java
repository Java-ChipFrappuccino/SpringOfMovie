package kr.co.moviespring.web.controller.api;

import kr.co.moviespring.web.config.security.CustomUserDetails;
import kr.co.moviespring.web.entity.CommunityBoard;
import kr.co.moviespring.web.entity.CommunityBoardView;
import kr.co.moviespring.web.service.CommunityBoardCommentsService;
import kr.co.moviespring.web.service.CommunityBoardService;
import kr.co.moviespring.web.service.MemberService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController("ApiCommunityBoardController")
@RequestMapping("api/community-board")
public class CommunityBoardController {

    @Autowired
    private CommunityBoardCommentsService commentsService;

    @Autowired
    private CommunityBoardService communityBoardService;

    @Autowired
    private MemberService memberService;


    // 댓글 수정 요청
    @PutMapping("comments/{commentId}")
    public int boardLike(@PathVariable Long commentId,@RequestBody String comment,
                         @AuthenticationPrincipal CustomUserDetails userDetails) {
//        @RequestBody LikeRequest LikeRequest
        if (userDetails == null)
            return 100; //로그인하지 않은 유저가 좋아요 누를시 100을 전송

        Long memberId = userDetails.getId();
        int result = commentsService.editById(commentId, memberId, comment);

        return result;
    }

    // 댓글 삭제 요청
    @DeleteMapping("comments/{commentId}")
    public int boardDisLike(@PathVariable Long commentId,
                            @AuthenticationPrincipal CustomUserDetails userDetails) {
//        @RequestBody LikeRequest LikeRequest
        if (userDetails == null)
            return 100; //로그인하지 않은 유저가 좋아요 누를시 100을 전송

//        Long memberId = userDetails.getId();
        int result = commentsService.deleteById(commentId);

        return result;
    }

    @DeleteMapping("delete-board")
    public ResponseEntity<String> deleteBoard(
        @RequestBody String[] idArr
    ){
        for(String strId : idArr){
            Long boardId = Long.parseLong(strId);
            communityBoardService.deleteById(boardId);
        }
        
        return ResponseEntity.ok("성공적으로 삭제되었습니다.");
    }

    @DeleteMapping("delete-comment")
    public ResponseEntity<String> deleteComment(
        @RequestBody String[] idArr
    ){
        for(String strId : idArr){
            Long boardId = Long.parseLong(strId);
            commentsService.deleteById(boardId);
        }
        
        return ResponseEntity.ok("성공적으로 삭제되었습니다.");
    }
}