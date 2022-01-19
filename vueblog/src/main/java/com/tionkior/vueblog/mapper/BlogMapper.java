package com.tionkior.vueblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tionkior.vueblog.entity.Blog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

}
