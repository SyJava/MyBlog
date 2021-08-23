package com.example.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blog.entity.Email;
import com.example.blog.mapper.EmailMapper;
import com.example.blog.service.EmailService;
import com.example.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sy
 * @since 2021-08-18
 */
@Service
public class EmailServiceImpl extends ServiceImpl<EmailMapper, Email> implements EmailService {
@Autowired
EmailMapper emailMapper;

    @Override
    public void addEmail(Email email) {
        emailMapper.insert(email);
    }

    @Override
    public Result getEmailByPage(Long current, Long size) {
        Page<Email> page = new Page<>(current,size);
        QueryWrapper<Email> qw = new QueryWrapper<>();
        Page<Email> emailPage = emailMapper.selectPage(page, qw);
        return Result.succ(emailPage);
    }

    @Override
    public Result deleteEmailById(Integer id) {
        emailMapper.deleteById(id);
        return Result.succ("邮件删除成功");
    }
}
