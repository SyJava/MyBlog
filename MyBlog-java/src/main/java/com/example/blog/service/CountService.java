package com.example.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog.entity.Count;
import com.example.blog.vo.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sy
 * @since 2021-08-22
 */
public interface CountService extends IService<Count> {
Result getCount();
}
