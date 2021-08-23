package com.example.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author sy
 * @since 2021-07-31
 */
@TableName("blog")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(indexName = "blog")//对应es中的索引名
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id  //必须有 id,这里的 id 是全局唯一的标识，等同于 es 中的"_id"
    @TableId(value = "blog_id", type = IdType.AUTO)
    private Integer blogId;

    @NotNull
    private Integer userId;
    /**
     * type : 字段数据类型
     * analyzer : 分词器类型
     * index : 是否索引(默认:true)
     * Keyword : 短语,不进行分词
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")//ik中文分词器
    @NotNull(message = "标题不能为空")
    private String title;

    @NotNull(message = "首图不能为空")
    private String firstPicture;//首图地址

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    @NotNull(message = "描述不能为空")
    private String description;//描述

    @NotNull(message = "内容不能为空")
    private String content;//内容

    @NotNull(message = "创作类型不能为空")
    private Boolean isOriginal;//是否原创，1为原创,0为转载

    private Boolean isPublished;//是否公开

    private Boolean shareStatement;//是否保存为草稿,1为草稿

    private Boolean isDelete;//逻辑删除,1为删除

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;//创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;//更新时间

    private Integer views;//浏览量

    private Integer commentCount;//评论数

    @NotNull(message = "分类不能为空")
    private Integer typeId;

    @TableField(exist = false) //查询时过滤非数据库字段
    private User user;//保存博客作者信息

    @TableField(exist = false) //查询时过滤非数据库字段
    private Type type;//保存博客作者信息

    @TableField(exist = false)  //查询时过滤非数据库字段
    private List<Tag> tags;

}
