package com.example.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blog.entity.Link;
import com.example.blog.mapper.LinkMapper;
import com.example.blog.service.LinkService;
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
 * @since 2021-08-18
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {
@Autowired
LinkMapper linkMapper;
    @Override
    public Result saveLink(Link link) {
        linkMapper.insert(link);
        return Result.succ("友链新增成功");    }

    @Override
    public Result getLinksByPage(Long current, Long size) {
        QueryWrapper<Link> qw = new QueryWrapper<>();
        Page<Link> linkPage = new Page<>();
        linkPage.setCurrent(current);
        linkPage.setSize(size);
        Page<Link> page = linkMapper.selectPage(linkPage, qw);
        return Result.succ(page);
    }

    @Override
    public Result deleteLinkById(Integer id) {
        linkMapper.deleteById(id);
        return Result.succ("友链删除成功");
    }

    @Override
    public Result updateLink(Link link) {
        linkMapper.updateById(link);
        return Result.succ("友链更新成功");
    }

    @Override
    public Result getAllLink() {
        QueryWrapper<Link> qw = new QueryWrapper<>();
        List<Link> links = linkMapper.selectList(qw);
        return Result.succ(links);
    }
}
