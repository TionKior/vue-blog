package com.tionkior.vueblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tionkior.vueblog.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
