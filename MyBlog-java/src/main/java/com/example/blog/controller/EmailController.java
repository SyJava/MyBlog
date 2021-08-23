package com.example.blog.controller;


import com.example.blog.entity.Email;
import com.example.blog.service.EmailService;
import com.example.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.HashMap;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sy
 * @since 2021-08-18
 */
@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    EmailService emailService;

    /**
     * 获取配置文件里的发送方
     */
    @Value("${spring.mail.username}")
    private String fromAddress;

    /**
     * 发送邮件
     * @param map
     * @return
     * @throws MessagingException
     */
  @RequestMapping("/sendMail")
  public Result sendMail(@RequestBody HashMap<String, String> map) throws MessagingException {
      // 设置邮件内容
    Email email = new Email();
    email.setSubject(map.get("subject"));
    email.setToAddress(map.get("email"));
    email.setContent(map.get("content"));
    email.setSendTime(new Date());
    email.setFromAddress(fromAddress);
    // 发送邮件
    send(email);
    //保存邮件
    emailService.addEmail(email);
    return Result.succ("邮件发送成功");

}
    /**
     * 发送邮件的方法
     */
    public void send(Email email) throws MessagingException {
        System.out.println("接收到的邮件对象为：" + email);

        //创建一封邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        //Spring提供的一个便捷的邮件设置对象
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        //设置邮件的内容
        //标题
        mimeMessageHelper.setSubject(email.getSubject());

        //发送方
        mimeMessageHelper.setFrom(email.getFromAddress());

        //接收方
        mimeMessageHelper.setTo(email.getToAddress());//接收者
//        mimeMessageHelper.setCc("***@qq.com");//抄送
//        mimeMessageHelper.setBcc("***@qq.com");//密送

        mimeMessageHelper.setText(email.getContent(),true);
        //发送附件
//      mimeMessageHelper.addAttachment("我的附件.jpg", new File("C:\\worker\\images\\7f25dc3b-13e5-42a5-a205-8276b0528040"));

        //设置当前时间
        mimeMessageHelper.setSentDate(email.getSendTime());

        //发送邮件
        javaMailSender.send(mimeMessage);
        System.out.println("成功发送邮件......");
    }

    /**
     * 分页获取邮件
     * @param current
     * @param size
     * @return
     */
    @GetMapping("/getEmailByPage")
    public Result getEmailByPage(Long current,Long size){
       return emailService.getEmailByPage(current,size);
    }
    @DeleteMapping("/deleteEmailById")
    public Result deleteEmailById(Integer id){
       return emailService.deleteEmailById(id);
    }
}


