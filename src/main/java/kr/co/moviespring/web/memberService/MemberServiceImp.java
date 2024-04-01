package kr.co.moviespring.web.memberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.moviespring.web.entity.Member;
import kr.co.moviespring.web.memberRepository.MemberRepository;

@Service
public class MemberServiceImp implements MemberService {

    @Autowired
    MemberRepository memberRepository;

    // 로그인정보 검사하기//
    @Override
    public boolean validate(String userId,String pwd) {
        Member member = memberRepository.findByMembername(userId);
        if (member == null) {
            return false;
        }

        if (!member.getPwd().equals(pwd)) {
            return false;
        }
        return true;
    }

    // 회원 가입//
    @Override
    public void regist(String userId, String pwd, String name, String nickname, int age, String email) {
        
        memberRepository.regist(userId, pwd, name, nickname, age, email);
    }

}
