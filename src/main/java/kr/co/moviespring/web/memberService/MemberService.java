package kr.co.moviespring.web.memberService;


public interface MemberService {
    //로그인 입력정보 검사//
    boolean validate(String userId , String pwd);

    //회원가입//
    void regist(String userId, String pwd, String nickname, String name, int age, String email);
    
}
