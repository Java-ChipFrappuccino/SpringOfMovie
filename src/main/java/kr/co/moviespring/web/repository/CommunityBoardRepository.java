package kr.co.moviespring.web.repository;

import java.util.List;

import kr.co.moviespring.web.entity.CommunityBoardView;
import org.apache.ibatis.annotations.Mapper;

import kr.co.moviespring.web.entity.CommunityBoard;

@Mapper
public interface CommunityBoardRepository {
    // 게시글 등록//
    // void saveContents(String title, String contents, Long categoryId);

    //nickname 붙여서 게시글등록
    void save(Long memberId, String title, String contents, Long categoryId);

    // 게시글 수정
    void update(Long id, String title, String contents);

    // 게시글 삭제//
    int delete(Long id);

    // 카테고리별 게시글 목록
    List<CommunityBoardView> findAllByCategoryId(Long categoryId, boolean isBest, String query, int offset, int size);

    //게시글 디테일페이지
    CommunityBoardView findById(Long id);

    //페이징을 위한 게시글 갯수 카운팅
    int getCount(Long memberId, Long categoryId, String query);

    // 아이디별 조회
    List<CommunityBoardView> findByMemberId(Long memberId);
    // 게시글 조회시마다 조회수 업데이트
    void updateHit(Long boardId);

    // 다음글 아이디 가져오기
    CommunityBoard findNextId(Long boardId, Long categoryId);
    // 이전글 아이디 가져오기
    CommunityBoard findPrevId(Long boardId, Long categoryId);
}
