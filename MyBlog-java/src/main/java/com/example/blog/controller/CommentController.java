package com.example.blog.controller;


import com.example.blog.entity.Comment;
import com.example.blog.service.CommentService;
import com.example.blog.service.UserService;
import com.example.blog.utils.IpUtil;
import com.example.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sy
 * @since 2021-08-15
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;

    //新增评论
    @PostMapping("/saveComments")
    public Result post(@RequestBody Comment comment,HttpServletRequest request) {
        //获取评论人的ip
        String ip = IpUtil.getIp(request);
        Integer blogId = comment.getBlogId();
        //判断是否为管理员评论
        if(comment.getAdminComment()){
            // 设置管理员头像
            comment.setAvatar("https://gitee.com/syfzy/my-blog-img/raw/master/img/20210816/1629116851713_3d69d71e-cd64-4258-a718-d3bf70ae6f6d.png");
        }else {
            //设置游客头像
            comment.setAvatar("https://gitee.com/syfzy/my-blog-img/raw/master/img/20210817/1629161359790_b677addd-ad97-408e-9203-1444ea9422c9.jpg");
        }

        comment.setIp(ip);
        comment.setCreateTime(LocalDateTime.now());
        return commentService.saveComment(comment);
    }

    //前台初始化评论
    @GetMapping("/getComments/{blogId}")
    public Result getCommentById(@PathVariable Integer blogId){
      return  commentService.getCommentById(blogId);
    }
    //后台初始化评论(分页查询)
    @GetMapping("/getCommentByPage")
    public Result getCommentByPage(Long current,Long size){
      return commentService.getCommentByPage(current,size);
    }
    //根据id删除评论
    @DeleteMapping("/deleteCommentById")
    public Result deleteCommentById(Integer id){
        return commentService.deleteCommentById(id);
    }

}

