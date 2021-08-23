package com.example.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog.entity.Comment;
import com.example.blog.vo.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sy
 * @since 2021-08-15
 */
public interface CommentService extends IService<Comment> {
 Result saveComment(Comment comment);
 Result getCommentById(Integer blogId);
 Result getCommentByPage(Long current,Long size);
 Result deleteCommentById(Integer id);
 void deleteCommentByBlogId(Integer id);
}
