package kr.co.moviespring.web.entity;

import java.util.Date;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnelineReviewView {

    private Long id;
    private Long movieId;
    private Long memberId;
    private Date regDate;
    private String comments;
    private int memberRate;
    private String nickname;
    private int likeCount; //한줄평의 총 좋아요 갯수
    private int likeStatus; //현재 로그인한 유저의 현재 한줄평 목록에 좋아요 여부
}
