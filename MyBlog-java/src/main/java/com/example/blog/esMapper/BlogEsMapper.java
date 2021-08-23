package com.example.blog.esMapper;

import com.example.blog.entity.Blog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 公众号：饮亿杯Java
 *
 * @author SY
 * @date 2021/8/20 11:21
 */
//elasticsearch数据访问层,使用BlogEsMapper实例即可操作es
@Repository
public interface BlogEsMapper extends ElasticsearchRepository<Blog, Long> {
}
