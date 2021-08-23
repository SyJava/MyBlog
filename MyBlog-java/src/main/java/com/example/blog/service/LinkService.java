package com.example.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog.entity.Link;
import com.example.blog.vo.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sy
 * @since 2021-08-18
 */
public interface LinkService extends IService<Link> {
Result saveLink(Link link);
Result getLinksByPage(Long current,Long size);
Result deleteLinkById(Integer id);
Result updateLink(Link link);
Result getAllLink();
}
