package com.example.blog.service;

import com.example.blog.entity.Email;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog.vo.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sy
 * @since 2021-08-18
 */
public interface EmailService extends IService<Email> {
    void addEmail(Email email);
    Result getEmailByPage(Long current, Long size);
    Result deleteEmailById(Integer id);
}
