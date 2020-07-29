package com.talk.demo.mapper;

import com.talk.demo.dto.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    @Insert("insert into user values (#{accountId}, #{name}, #{token}, #{gmtCreate}, #{gmtModify}, #{id})")
    void insertUser(User user);
}
