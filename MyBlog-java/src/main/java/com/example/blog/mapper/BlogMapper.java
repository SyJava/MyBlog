package com.example.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blog.entity.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sy
 * @since 2021-07-31
 */
@Mapper
public interface BlogMapper extends BaseMapper<Blog> {
//前台分页获取博客列表
    Page<Blog> vuefindByPage(Page<Blog> page,@Param("typeId") Integer typeId,@Param("tagId")Integer tagId);
// 根据id获取博客
    Blog getByBlogId(Integer id);
}
