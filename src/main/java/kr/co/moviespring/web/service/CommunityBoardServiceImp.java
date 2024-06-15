package kr.co.moviespring.web.service;

import kr.co.moviespring.web.entity.CommunityBoard;
import kr.co.moviespring.web.entity.CommunityBoardView;
import kr.co.moviespring.web.repository.CommunityBoardRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityBoardServiceImp implements CommunityBoardService {

    @Autowired
    CommunityBoardRepository communityBoardRepository;

    // 게시글 등록//
    @Override
    public void write(Long memberId,String title, String contents,Long categoryId) {
        communityBoardRepository.save(memberId,title,contents,categoryId);
    }
    
    // 게시글 삭제//
    @Override
    public int deleteById(Long id) {
        int result = communityBoardRepository.delete(id);
        return result;
    }
    
    // 게시글 수정
    @Override
    public void editById(Long id, String title, String contents) {
        communityBoardRepository.update(id, title, contents);
    }

    //인기글 카테고리+검색쿼리값 게시글 목록//
    @Override
    public List<CommunityBoardView> getList(Long categoryId, Integer page, Integer size, String query, boolean isBest) {
        //int size = 20;
        int offset = (page-1)*size;
        List<CommunityBoardView> list = communityBoardRepository.findAllByCategoryId(categoryId, isBest, query, offset, size);
        return list;
    }

    //카테고리별 게시글 목록//
    @Override
    public List<CommunityBoardView> getList(Long categoryId, Integer page, Integer size) {
        return getList(categoryId, page, size, null, false);
    }

    //인기글 카테고리 게시글 목록//
    @Override
    public List<CommunityBoardView> getList(Long categoryId, Integer page, Integer size, boolean isBest) {
        return getList(categoryId, page, size, null, isBest);
    }

    //카테고리별+검색쿼리값 게시글 목록//
    @Override
    public List<CommunityBoardView> getList(Long categoryId, Integer page, Integer size, String query) {
        return getList(categoryId, page, size, query, false);
    }

    //게시글 상세//
    // @Override
    // public CommunityBoard getById(Long id) {
    //     CommunityBoard board = communityBoardRepository.findById(id);
    //     return board;
    // }

    //게시글 상세 사용자 닉네임추가//
    @Override
    public CommunityBoardView getById(Long id) {
        CommunityBoardView board = communityBoardRepository.findById(id);
        return board;
    }
    // 다음글 아이디 가져오기
    @Override
    public CommunityBoard getNextId(Long boardId, Long categoryId) {
        CommunityBoard nextBoard = communityBoardRepository.findNextId(boardId,categoryId);
        return nextBoard;
    }
    // 이전글 아이디 가져오기
    @Override
    public CommunityBoard getPrevId(Long boardId, Long categoryId) {
        CommunityBoard prevBoard = communityBoardRepository.findPrevId(boardId,categoryId);
        return prevBoard;
    }

    

    @Override
    public int getCount(Long memberId) {
        return getCount(memberId, null, null);
    }

    @Override
    public int getCount(Long memberId, Long categoryId) {
        return getCount(memberId, categoryId, null);
    }

    @Override
    public int getCount(Long memberId, Long categoryId, String query) {
        int count = communityBoardRepository.getCount(memberId, categoryId, query);
        return count;
    }

//    @Override
//    public List<GeneralBoard> getList() {
//        List<GeneralBoard> list = communityRepository.findAll();
//        return list;
//    }
}
