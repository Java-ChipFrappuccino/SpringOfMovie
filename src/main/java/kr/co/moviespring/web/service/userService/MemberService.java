package kr.co.moviespring.web.service.userService;


import kr.co.moviespring.web.entity.Member;

public interface MemberService {
    //로그인 입력정보 검사//
//    boolean validate(String username , String password); //스프링 시큐리티로 인해 불필요?

    //회원가입//
    void regist(Member member);
//    String userId, String pwd, String nickname, String name, int age, String email


    
}