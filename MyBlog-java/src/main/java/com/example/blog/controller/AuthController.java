package com.example.blog.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.example.blog.utils.RedisUtil;
import com.example.blog.vo.Const;
import com.example.blog.vo.Result;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
@RestController
public class AuthController {
    @Autowired
    Producer producer;
    @Autowired
    RedisUtil redisUtil;
    @GetMapping("captcha")
    public Result Captcha() throws IOException {
        String key = UUID.randomUUID().toString();
        String code = producer.createText();//生成验证码字符串

        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);

        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";

        String base64Img = str + encoder.encode(outputStream.toByteArray());

    redisUtil.hset(Const.CAPTCHA, key, code, 120); // 验证码保存两分钟
        return Result.succ(
                MapUtil.builder()
                        .put("token", key)
                        .put("captchaImg", base64Img)
                        .build()

        );
    }
}
