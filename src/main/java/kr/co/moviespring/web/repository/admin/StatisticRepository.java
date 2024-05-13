package kr.co.moviespring.web.repository.admin;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StatisticRepository {
    int CountAllMember();

    int CountTodayRegMember();

    int CountMemberAgeRange(int count);
}
