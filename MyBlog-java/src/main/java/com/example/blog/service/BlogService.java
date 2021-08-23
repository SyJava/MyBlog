package com.example.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog.entity.Blog;
import com.example.blog.vo.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sy
 * @since 2021-07-31
 */
 public interface BlogService extends IService<Blog> {
   /**
    * 保存博客
    */
     void SavaBlog(Blog blog);

    /**
     * 更新博客
     * @param blog
     */
     void UpdateBlog(Blog blog);

    /**
     * 后台分页查询
     * @param
     * @return
     */
    Result findByPage(Long current, Long size,Boolean isOriginal, Boolean isPublished,
                             Boolean shareStatement, Boolean isDelete);

    /**
     * 前台分页查询
     * @return
     */
    Result vuefindByPage(Long current,Long size,Integer typeId,Integer tagId);

 /**
  * 查询热门文章
  * @return
  */
    Result vuefindHotBlog();
    /**
     * 根据id查询博客
     * @param id
     * @return
     */
     Result getByBlogId(Integer id);
    /**
     * 逻辑删除
     * @param id
     * @return
     */
    Result logicDeleteBlog(Integer id);

    /**
     * 彻底删除
     * @param id
     * @return
     */
    Result deleteBlog(Integer id);

    /**
     * 还原博客
     * @param id
     * @return
     */
    Result recoveryBlog(Integer id);

    /**
     * 根据title或type搜素博客
     * @param search
     * @return
     */
    Result searchBlogByValue(String search);

}
