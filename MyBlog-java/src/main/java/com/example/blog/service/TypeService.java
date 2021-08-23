package com.example.blog.service;

import com.example.blog.entity.Type;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog.vo.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sy
 * @since 2021-08-07
 */
public interface TypeService extends IService<Type> {
    Result getAllType();
    Result getTypeByPage(Long current,Long size);
    Result addType(String name);
    Result deleteType(Integer id);
    Result updateType(Type type);
}
