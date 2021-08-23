package com.example.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog.entity.Tag;
import com.example.blog.vo.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sy
 * @since 2021-08-12
 */
public interface TagService extends IService<Tag> {
    Result getAllTag();
    Result getTagByPage(Long current,Long size);
    Result addTag(String name);
    Result deleteTag(Integer id);
    Result updateTag(Tag type);
}
