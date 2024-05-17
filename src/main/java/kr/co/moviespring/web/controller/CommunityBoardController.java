package kr.co.moviespring.web.controller;

import java.util.List;

import kr.co.moviespring.web.entity.*;
import kr.co.moviespring.web.repository.CommunityBoardRepository;
import kr.co.moviespring.web.service.CommunityBoardCommentsService;
import kr.co.moviespring.web.service.CommunityBoardLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import kr.co.moviespring.web.config.security.CustomUserDetails;
import kr.co.moviespring.web.service.CategoryService;
import kr.co.moviespring.web.service.CommunityBoardService;



@Controller
@RequestMapping("community")
public class CommunityBoardController {
    @Autowired
    CommunityBoardService communityBoardService;
    @Autowired
    CommunityBoardLikeService communityBoardLikeService;
    @Autowired
    CommunityBoardCommentsService communityBoardCommentsService;
    @Autowired
    CommunityBoardRepository communityBoardRepository;
    @Autowired
    CategoryService categoryService;


    // 커뮤니티 메인페이지 요청//
    @GetMapping("main")
    public String main(Model model){
        List<Category> categories = categoryService.getList();

        for (int i = 0; i < categories.size(); i++) {
            Long categoryId = categories.get(i).getId();
            List <CommunityBoardView> list = communityBoardService.getList(categoryId,1, 5);
            model.addAttribute("list"+(i+1), list); // list1,2,3,4,5로 보내려고 +1해줌 특별한 의미 없음
        }
        model.addAttribute("categories", categories);

        return "community/main";
    }


//    게시글 목록 요청//
    @GetMapping("board/list")
    public String list(@RequestParam(name = "c",required = false) String categoryName,
                        @RequestParam(name = "q",required = false) String query,
                        @RequestParam(name = "p",required = false, defaultValue = "1") Integer page,
                        Model model){
        Category category = categoryService.getByName(categoryName);
        Long categoryId = category.getId();
        List <CommunityBoardView> list = communityBoardService.getList(categoryId, page, 20);
        int count = 0;
        count = communityBoardService.getCount(categoryId);


        if (query != null) {
            list = communityBoardService.getList(categoryId, page, 20, query);
            count = communityBoardService.getCount(categoryId, query);
        }

        List<Category> categories = categoryService.getList();
        model.addAttribute("list", list);
        model.addAttribute("count", count);
        model.addAttribute("c", category);
        model.addAttribute("ctgList", categories);

        return "community/board/list";
    }

    //    게시글 목록 요청 연습용 임시코드//
    @GetMapping("board/list-vue")
    public String listVue(@RequestParam(name = "c",required = false) String categoryName,
                        @RequestParam(name = "q",required = false) String query,
                        @RequestParam(name = "p",required = false, defaultValue = "1") Integer page,
                        Model model){
        Category category = categoryService.getByName(categoryName);
        Long categoryId = category.getId();
        List <CommunityBoardView> list = communityBoardService.getList(categoryId, page, 20);
        int count = 0;
        count = communityBoardService.getCount(categoryId);


        if (query != null) {
            list = communityBoardService.getList(categoryId, page, 20, query);
            count = communityBoardService.getCount(categoryId, query);
        }

        List<Category> categories = categoryService.getList();
        model.addAttribute("list", list);
        model.addAttribute("count", count);
        model.addAttribute("c", category);
        model.addAttribute("ctgList", categories);

        return "community/board/list-vue";
    }

    //게시글 상세페이지 요청//
    @GetMapping("board/detail")
    
    public String detail(@RequestParam("c")String categoryName,
                         @RequestParam("id")Long boardId, Model model,@AuthenticationPrincipal CustomUserDetails userDetails){
        //FIXME: 2024-04-14, 일, 22:58 주소창에서 카테고리 쿼리를 변경해도 게시글이 그대로 출력되는 버그있음,
        // 임의로 변경시 게시글의 카테고리명이 변경됨, 쿼리값에 카테고리가 필요한지? (디씨의 경우 있긴 함) -JOON
        Long memberId = null;
        if(userDetails != null)
        memberId= userDetails.getId();

        CommunityBoardView board = communityBoardService.getById(boardId);
        List<CommunityBoardCommentsView> list = communityBoardCommentsService.getListById(boardId);
        communityBoardRepository.updateHit(boardId); //게시글 조회시마다 조회수 1증가 로직, 서비스단이 필요없는것같아 리포지토리 바로 호출
        // CommunityBoard board = communityBoardService.getById(boardId); 예전버전
        Category category = categoryService.getByName(categoryName);
//        Integer likeCount = communityBoardLikeService.getCount(boardId, 1); //2번째 인자값 => 1 = 좋아요 개수 / 2 = 싫어요 개수 / 생략 = 총 개수
        Integer disLikeCount = communityBoardLikeService.getCount(boardId, -1);

        model.addAttribute("memberId", memberId);
        model.addAttribute("board", board);
        model.addAttribute("list", list);
        model.addAttribute("category", category);
//        model.addAttribute("likeCount", likeCount); //보드view에 좋아요 개수가 담겨서 오므로 필요 없어짐
        model.addAttribute("disLikeCount", disLikeCount);

        return "community/board/detail";
    }
    // 게시글 등록페이지 요청//
    @GetMapping("board/reg")
    public String reg(@RequestParam(name="c",required = false)String categoryName,
                      @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        //비회원이 게시글 등록링크 요청시 해당 게시글리스트로 리다이렉트
        if (userDetails == null)
            return "redirect:/community/board/list?c="+categoryName;

        model.addAttribute("cName", categoryName);
        return "community/board/reg";
    }

    // 게시글 등록//
    @PostMapping("board/reg")
    public String reg(String title , String contents, String categoryName,@AuthenticationPrincipal CustomUserDetails userDetails){
        Category category = categoryService.getByName(categoryName);

        Long memberId = null;
        if(userDetails != null)
        memberId= userDetails.getId();
        
        communityBoardService.write(memberId,title,contents,category.getId());
        System.out.println("등록");
        return "redirect:/community/board/list?c="+categoryName;
    }

    // 게시글 댓글 등록//
    @PostMapping("board/detail")
    public String regComment(String contents,@RequestParam(name="c",required = false)String categoryName,
                                             @RequestParam(name="id",required = false)Long boardId,
                                             @RequestParam(name="memberId",required = false)Long memberId,
                                             @RequestParam(name="content",required = false)String content){
//        communityBoardService.write(contents,categoryId);
//        String cName = categoryName;
//        System.out.printf("%s %d %d %s",categoryName,boardId,memberId,content);
//        return "redirect:/community/main";
        communityBoardCommentsService.write(boardId,memberId,content);
        return "redirect:/community/board/detail?c="+categoryName+"&id="+boardId;
    }

    // 게시글 등록 수정페이지 요청//
    @GetMapping("board/edit/{id}")
    public String edit(@PathVariable Long id,@RequestParam(name="c",required = false)String categoryName,
                       @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        // CommunityBoard board = communityBoardService.getById(id);
        //비회원이 수정링크 요청시 해당 게시글페이지로 리다이렉트
        if (userDetails == null)
            return "redirect:/community/board/detail?c="+categoryName+"&id="+id;
//        userDetails
        //글작성자가 아닌 회원이 수정링크 요청시 해당 게시글페이지로 리다이렉트
        CommunityBoardView board = communityBoardService.getById(id);
        if (userDetails.getId() != board.getMemberId())
            return "redirect:/community/board/detail?c="+categoryName+"&id="+id;

        model.addAttribute("board", board);
        model.addAttribute("cName", categoryName);
        return "community/board/reg";
    }

    // 게시글 수정// //put으로 바꿔야함
    @PostMapping("board/edit/{id}")
    public String edit(String contents , String title, @PathVariable Long id, String categoryName) {
        communityBoardService.editById(id, title, contents);
//        model.addAttribute("board", board);
        return "redirect:/community/board/detail?c="+categoryName+"&id="+id;
    }

    // 게시글 삭제// //delete로 바꿔야함
    @GetMapping("board/delete/{id}")
    public String delete(@PathVariable Long id,@RequestParam(name="c",required = false)String categoryName,
                         @AuthenticationPrincipal CustomUserDetails userDetails) {

        //비회원이 삭제 요청시 해당 게시글페이지로 리다이렉트
        if (userDetails == null)
            return "redirect:/community/board/detail?c="+categoryName+"&id="+id;
//        userDetails
        //글작성자가 아닌 회원이 삭제 요청시 해당 게시글페이지로 리다이렉트
        CommunityBoardView board = communityBoardService.getById(id);
        if (userDetails.getId() != board.getMemberId())
            return "redirect:/community/board/detail?c="+categoryName+"&id="+id;

        int result = communityBoardService.deleteById(id);
        return "redirect:/community/board/list?c="+categoryName;
    }
}
