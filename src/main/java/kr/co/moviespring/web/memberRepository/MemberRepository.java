package kr.co.moviespring.web.memberRepository;

import org.apache.ibatis.annotations.Mapper;

import kr.co.moviespring.web.entity.Member;

@Mapper
public interface MemberRepository {
    Member findByMembername(String userId);

    void regist(String userId, String pwd, String name, String nickname, int age, String email);
  
}
