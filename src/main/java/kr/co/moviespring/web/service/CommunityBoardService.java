package kr.co.moviespring.web.service;

import java.util.List;

import kr.co.moviespring.web.entity.CommunityBoard;
import kr.co.moviespring.web.entity.CommunityBoardView;

public interface CommunityBoardService {

    // 게시글 등록//
    // void write(String title, String contents, Long categoryId);
    // 게시글 삭제
    int deleteById(Long id);
    // 게시글 수정
    void editById(Long id, String title, String contents);
    //카테고리별 게시글 목록//
    List<CommunityBoardView> getList(Long categoryId, Integer page, Integer size);
    List<CommunityBoardView> getList(Long categoryId, Integer page, Integer size, boolean isBest);
    List<CommunityBoardView> getList(Long categoryId, Integer page, Integer size, String query);
    List<CommunityBoardView> getList(Long categoryId, Integer page, Integer size, String query, boolean isBest);
    //게시글 상세//
    CommunityBoardView getById(Long id);
    // CommunityBoard getById(Long id);
    //다음글 아이디 가져오기
    CommunityBoard getNextId(Long boardId, Long categoryId);
    //이전글 아이디 가져오기
    CommunityBoard getPrevId(Long boardId, Long categoryId);

    int getCount(Long memberId);
    int getCount(Long memberId, Long categoryId);
    int getCount(Long memberId, Long categoryId, String query);
    
    void write(Long memberId, String title, String contents, Long id);

    // 모든 게시물 목록 필요한가? 잘 모르겠음 일단 주석처리 2024-04-14, 일, 21:59  -JOON
//    List<GeneralBoard> getList();

}
