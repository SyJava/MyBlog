package com.example.blog.mapper;

import com.example.blog.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sy
 * @since 2021-08-15
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}
