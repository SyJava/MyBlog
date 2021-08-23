package com.example.blog.controller;

import com.example.blog.entity.Blog;
import com.example.blog.service.BlogService;
import com.example.blog.service.CommentService;
import com.example.blog.service.TagService;
import com.example.blog.service.UserService;
import com.example.blog.utils.IpUtil;
import com.example.blog.utils.RedisUtil;
import com.example.blog.vo.Const;
import com.example.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 公众号：饮亿杯Java
 * @author SY
 * @date 2021/7/31 9:59
 */
@RestController
@RequestMapping("blog")
public class BlogController {
    @Autowired
    UserService userService;
    @Autowired
    BlogService blogService;
    @Autowired
    TagService tagService;
    @Autowired
    CommentService commentService;
    @Autowired
    RedisUtil redisUtil;
    /**
     * 搜索（高亮显示)
     * @param value
     * @return
     */
    @GetMapping("/search")
    public Result search(String value){
        return blogService.searchBlogByValue(value);
    }

    /**
     * 发布博客,需要经过validation验证
     * @param blog
     * @return
     */
    @PostMapping("/savaBlog")
public Result SavaBlog(@RequestBody @Validated Blog blog){
        blog.setShareStatement(false);
        blog.setCreateTime(new Date());//创建博客时间
        blogService.SavaBlog(blog);
        return Result.succ("博客发布成功");
}

    /**
     * 更新博客（更新后处于发布状态）
     * @param blog
     * @return
     */
    @PutMapping("/updateBlog")
public  Result updateBlog(@RequestBody @Validated Blog blog){
        blog.setShareStatement(false);
        blog.setUpdateTime(new Date());//更新博客时间
        blogService.UpdateBlog(blog);
        return Result.succ("更新成功");
}

    /**
     * 保存草稿，不用经过validate验证
     * @param blog
     * @return
     */
    @RequestMapping("/temporarySave")
    public Result TemporarySava(@RequestBody  Blog blog){
 //以下条件说明，博客存在且为草稿，所以不用另存一份草稿，直接修改即可，并修改更新时间
    if(blog.getBlogId()!=null&&blog.getShareStatement()){
         blog.setUpdateTime(new Date());
         blogService.UpdateBlog(blog);
    }else {//博客不存在，则存为一份草稿
         blog.setCommentCount(0);//评论数清空
         blog.setViews(0);//浏览量清空
         blog.setShareStatement(true);//保存草稿
         blog.setCreateTime(new Date());//创建草稿时间
         blog.setUpdateTime(null);//因为是另存了一份新草稿，所以不存在更新时间
         blogService.SavaBlog(blog);
    }
         return Result.succ("草稿保存成功");
    }

    /**
     *后台分页查询
     * @param
     * @return
     */
    @RequestMapping("/getByPage")
     public Result FindByPage(Long current, Long size, Boolean isOriginal, Boolean isPublished,
                              Boolean shareStatement, Boolean isDelete){
        return blogService.findByPage(current, size, isOriginal, isPublished,
                shareStatement, isDelete);
    }

    /**
     * 前台分页查询
     * @param current
     * @param size
     * @return
     */
    @RequestMapping("/vuefindByPage")
    public Result vuefindByPage(Long current, Long size, Integer typeId,Integer tagId,HttpServletRequest request){
        String ip = IpUtil.getIp(request);
        redisUtil.sSet(Const.VIEWS,ip);//将ip存入redis，访问量即数据总量，因为Set不会存放相同数据，所以一个ip就是一个访问量
        return blogService.vuefindByPage(current,size,typeId,tagId);
    }

    /**
     * 查询热门视频top5
     * @return
     */
    @GetMapping("/vuefindHotBlog")
    public Result vuefindHotBlog(){
    return blogService.vuefindHotBlog();
    }
    /**
     * 根据id查询博客具体内容
     * @param id
     * @return
     */
    @GetMapping("/getByBlogId")
    public Result getByBlogId(Integer id){
    return blogService.getByBlogId(id);
    }
    /**
     * 逻辑删除
     * @param id：博客id
     * @return
     */
    @DeleteMapping("/logicDeleteBlog")
    public Result logicDeleteBlog(Integer id){ return blogService.logicDeleteBlog(id); }

    /**
     *彻底删除博客
     * @param id
     * @return
     */
    @DeleteMapping("deleteBlog")
    public Result deleteBlog(Integer id){
        // 删除博客下的全部评论
        commentService.deleteCommentByBlogId(id);
        return blogService.deleteBlog(id);
    }

    /**
     * 还原博客
     * @param id
     * @return
     */
    @PostMapping("recoveryBlog")
    public Result recoveryBlog(Integer id){
        return blogService.recoveryBlog(id);
    }
}
