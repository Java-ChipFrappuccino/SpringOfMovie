package kr.co.moviespring.web.repository;

import org.apache.ibatis.annotations.Mapper;

import kr.co.moviespring.web.entity.Member;

@Mapper
public interface MemberRepository {
    Member findByMembername(String username);

    void regist(Member member);
//    String username, String password, String name, String nickname, int age, String email
  
}