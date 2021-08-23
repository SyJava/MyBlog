package com.example.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blog.entity.BlogTag;
import com.example.blog.entity.Tag;
import com.example.blog.mapper.BlogTagMapper;
import com.example.blog.mapper.TagMapper;
import com.example.blog.service.TagService;
import com.example.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sy
 * @since 2021-08-12
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Autowired
    TagMapper tagMapper;
    @Autowired
    BlogTagMapper blogTagMapper;
    @Override
    public Result getAllTag() {
        QueryWrapper<Tag> qw = new QueryWrapper<>();
        List<Tag> tags = tagMapper.selectList(qw);
        return Result.succ(tags);
    }

    @Override
    public Result getTagByPage(Long current, Long size) {
        QueryWrapper<Tag> qw = new QueryWrapper<>();
        Page<Tag> tagPage = new Page<>();
        tagPage.setCurrent(current);
        tagPage.setSize(size);
        Page<Tag> page = tagMapper.selectPage(tagPage, qw);
        return Result.succ(page);
    }

    @Override
    public Result addTag(String name) {
        Tag tag = new Tag();
        tag.setTagName(name);
        tagMapper.insert(tag);
        return Result.succ("成功新增标签");
    }

    @Override
    public Result deleteTag(Integer id) {
        tagMapper.deleteById(id);
        QueryWrapper<BlogTag> qw = new QueryWrapper<>();
        qw.eq("tag_id",id);
        //删除博客与标签的映射关系
        blogTagMapper.delete(qw);
        return Result.succ("标签删除成功");
    }

    @Override
    public Result updateTag(Tag tag) {
        tagMapper.updateById(tag);
        return Result.succ("更新标签成功");
    }
}
