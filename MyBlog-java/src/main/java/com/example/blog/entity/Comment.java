package com.example.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author sy
 * @since 2021-08-15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 是否为管理员评论
     */
    private Boolean adminComment;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 邮箱
     */
    private String email;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 博客id
     */
    private Integer blogId;

    /**
     * 父评论id
     */
    private Integer parentCommentId;

    //回复评论
    @TableField(exist = false) //查询时过滤非数据库字段
    private List<Comment> replyComments = new ArrayList<>();
    @TableField(exist = false) //查询时过滤非数据库字段
    private Comment parentComment;
    @TableField(exist = false) //查询时过滤非数据库字段
    private String parentNickname;

}
