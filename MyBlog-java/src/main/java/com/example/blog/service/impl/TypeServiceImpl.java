package com.example.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blog.entity.Type;
import com.example.blog.mapper.TypeMapper;
import com.example.blog.service.TypeService;
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
 * @since 2021-08-07
 */
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {
    @Autowired
    TypeMapper typeMapper;
    QueryWrapper<Type> qw = new QueryWrapper<>();

    /**
     * 获取全部分类
     * @return
     */
    @Override
    public Result getAllType() {
        List<Type> types = typeMapper.selectList(qw);
        qw.clear();
        return Result.succ(types);
    }

    @Override
    public Result getTypeByPage(Long current, Long size) {
        Page<Type> typePage = new Page<>(current,size);
        Page<Type> page = typeMapper.selectPage(typePage, qw);
        return Result.succ(page);
    }

    @Override
    public Result addType(String name) {
        Type type = new Type();
        type.setName(name);
        typeMapper.insert(type);
        return Result.succ("成功新增分类");
    }

    @Override
    public Result deleteType(Integer id) {
        typeMapper.deleteById(id);
        return Result.succ("分类删除成功");
    }

    @Override
    public Result updateType(Type type) {
        typeMapper.updateById(type);
        return Result.succ("更新分类成功");
    }


}
