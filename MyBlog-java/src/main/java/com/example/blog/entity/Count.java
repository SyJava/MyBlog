package com.example.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 公众号：饮亿杯Java
 * @author SY
 * @date 2021/8/21 8:14
 */
@Data
@NoArgsConstructor
@ToString
public class Count {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    int blogCount;
    int views;
    int messageCount;
    public Count(int id) {
        this.id = id;
    }
    public Count(int blogCount, int views, int messageCount) {
        this.blogCount = blogCount;
        this.views = views;
        this.messageCount = messageCount;
    }
}
