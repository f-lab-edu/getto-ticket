package com.flab.gettoticket.auth.mapper;

import com.flab.gettoticket.auth.dto.UserRequest;
import com.flab.gettoticket.auth.dto.UserResponse;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    //사용자 정보
    UserResponse selectUserInfo(String email);

    //회원가입
    int insertUser(UserRequest userRequest);
}
