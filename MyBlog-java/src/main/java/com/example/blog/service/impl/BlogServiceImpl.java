package com.example.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blog.entity.Blog;
import com.example.blog.entity.BlogTag;
import com.example.blog.entity.Tag;
import com.example.blog.esMapper.BlogEsMapper;
import com.example.blog.mapper.BlogMapper;
import com.example.blog.mapper.BlogTagMapper;
import com.example.blog.mapper.TagMapper;
import com.example.blog.service.BlogService;
import com.example.blog.utils.RedisUtil;
import com.example.blog.vo.Const;
import com.example.blog.vo.Result;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sy
 * @since 2021-07-31
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
    @Autowired
    BlogMapper blogMapper;
    @Autowired
    TagMapper tagMapper;
    @Autowired
    BlogTagMapper blogTagMapper;
    @Autowired
    ElasticsearchRestTemplate restTemplate;
    @Autowired
    BlogEsMapper blogEsMapper;
    @Autowired
    RedisUtil redisUtil;
    //发布博客
    @Override
    @Transactional
    public void SavaBlog(Blog blog) {
        //保存博客
        blogMapper.insert(blog);
        //保存标签
        SaveBlogTag(blog);
        if(!blog.getShareStatement()){//如果保存的博客不是草稿，则存到es中。因为只有非草稿才能被搜索
            Blog blog1 = blogMapper.getByBlogId(blog.getBlogId());//这里的blog包含user，type，tag等对象信息
            blogEsMapper.save(blog1);//存放到es中
            redisUtil.incr(Const.BLOGCOUNT,1);//redis中博客数量+1
        }
    }
    //更新博客
    @Override
    @Transactional//开启事务，保证redis和mysql同步修改
    public void UpdateBlog(Blog blog) {
    //发布更新的草稿时，文章数加1，如果直接在原有博客基础上进行修改发布，文章数不增加
        if(blog.getShareStatement()){
            redisUtil.incr(Const.BLOGCOUNT,1);//redis中博客数量+1
        }
        blogMapper.updateById(blog);
        //保存标签
        SaveBlogTag(blog);
        Blog blog1 = blogMapper.getByBlogId(blog.getBlogId());//这里的blog包含user，type，tag等对象信息
        blogEsMapper.save(blog1);//更新es中的博客信息
    }

    //后台分页查询
    @Override
    public Result findByPage(Long current, Long size,Boolean isOriginal, Boolean isPublished,
                             Boolean shareStatement, Boolean isDelete) {
        QueryWrapper<Blog> qw = new QueryWrapper<>();
        Page<Blog> page = new Page<>();
//        构建查询条件
        if (isPublished != null){
            //构建条件
            qw.eq("is_published",isPublished);
        }
        if (isOriginal!= null){
            //构建条件
            qw.eq("is_original",isOriginal);
        }
        if (shareStatement != null){
            //构建条件
            qw.eq("share_statement",shareStatement);
        }
        if (isDelete != null){
            //构建条件
            qw.eq("is_delete",isDelete);
        }
        qw.orderByDesc("create_time");
        page.setCurrent(current);
        page.setSize(size);
        Page<Blog> blogPage = blogMapper.selectPage(page, qw);
        //直接返回Page的实例即可
        return Result.succ(blogPage);
    }

    /**
     * 前台分页获取博客列表
     * @param current
     * @param size
     * @return
     */
    @Override
    public Result vuefindByPage(Long current, Long size,Integer typeId,Integer tagId) {
        Page<Blog> page = new Page<>(current,size);
        Page<Blog> blogPage = blogMapper.vuefindByPage(page,typeId,tagId);//把分页条件作为参数传递，即可实现分页查询
        return Result.succ(blogPage);
    }

    @Override
    public Result vuefindHotBlog() {
        QueryWrapper<Blog> qw = new QueryWrapper<>();
        qw.orderByDesc("views");//按照浏览量由高到低排序
        qw.last("limit 0,5");//取出前5个
        qw.eq("is_delete",false);
        qw.eq("is_published",true);
        qw.eq("share_statement",false);
        List<Blog> blogs = blogMapper.selectList(qw);
        return Result.succ(blogs);
    }

    /**
     * 根据id获取博客，即查看博客详情
     * @param id
     * @return
     */
    @Override
    public Result getByBlogId(Integer id) {
        Blog blog = blogMapper.getByBlogId(id);
        //通过id获取博客，即为点开博客进行查看，所以浏览量+1
        blog.setViews(blog.getViews()+1);
        blogMapper.updateById(blog);
        System.out.println("博客"+blog);
        return Result.succ(blog);
    }

    /**
     * 逻辑删除
     * @param id
     * @return
     */
    @Override
    public Result logicDeleteBlog(Integer id) {
        /**
         * MP在进行更新操作时，会更新所有非null的属性值
         * 所以只需要设置id和删除状态
         */
        Blog blog = new Blog();
        blog.setBlogId(id);
        blog.setIsDelete(true);
        blogMapper.updateById(blog);
        redisUtil.decr(Const.BLOGCOUNT,1);//redis中博客数量-1
        return Result.succ("博客已存放到回收站");
    }

    /**
     * 彻底删除
     * @param id
     * @return
     */
    @Override
    public Result deleteBlog(Integer id) {
        blogMapper.deleteById(id);
        QueryWrapper<BlogTag> qw = new QueryWrapper<>();
        qw.eq("blog_id",id);
        blogTagMapper.delete(qw);//删除博客与标签的映射关系
        blogEsMapper.deleteById((long)id);//删除es中的博客信息
        return Result.succ("博客已彻底删除");
    }

    /**
     * 还原博客
     * @param id
     * @return
     */
    @Override
    public Result recoveryBlog(Integer id) {
        Blog blog = new Blog();
        blog.setBlogId(id);
        blog.setIsDelete(false);
        blogMapper.updateById(blog);
        redisUtil.incr(Const.BLOGCOUNT,1);//redis中博客数量+1
        return Result.succ("");
    }

    //保存博客标签列表，如果数据库中有该标签则不用再次保存
    public void  SaveBlogTag(Blog blog){
        //查询数据库中所有的标签
        QueryWrapper<Tag> qw = new QueryWrapper<>();
        List<Tag> tagList = tagMapper.selectList(qw);
        //获取前端传来的标签列表
        List<Tag> tags = blog.getTags();
        BlogTag blogTag = new BlogTag();
        int flag=0;//用来表示tags和tagList中是否有相同标签，1表示有
        Integer tagId = null;//重复的标签id
        for (Tag tag : tags) {
        //如果标签存在id，则表明该标签是在修改博客信息时从后端获取的，博客与标签的映射关系已经存在，不用再次保存
            if (tag.getTagId()!=null){
                continue;
            }
            for (Tag tag1 : tagList) {
                //出现重复标签
                if (tag.getTagName().equals(tag1.getTagName())) {
                    flag=1;
                    //记录重复标签id
                    tagId=tag1.getTagId();
                    break;
                }
            }
            // 标签未重复，则先存储到数据库，再保存新标签和博客的映射关系
            if(flag==0){
                //保存标签
                tagMapper.insert(tag);
                blogTag.setTagId(tag.getTagId());
            }else {//标签重复，直接保存该标签与博客的映射关系
                blogTag.setTagId(tagId);
            }
            blogTag.setBlogId(blog.getBlogId());

            //保存博客与标签映射表
            blogTagMapper.insert(blogTag);
            flag=1;
        }
    }

  /**
   * 使用elasticsearch进行高亮分页搜索
   * @param value
   * @return
   */
  @Override
  public Result searchBlogByValue(String value) {
      //根据一个值查询多个字段  并高亮显示  这里的查询是取并集，即多个字段只需要有一个字段满足即可
      //需要查询的字段，根据博客的title和description进行查询
      BoolQueryBuilder boolQueryBuilder= QueryBuilders.boolQuery()
              .should(QueryBuilders.matchQuery("title",value))
              .should(QueryBuilders.matchQuery("description",value));
    // 构建高亮查询
    NativeSearchQuery searchQuery =
        new NativeSearchQueryBuilder()
            .withQuery(boolQueryBuilder)
            .withHighlightFields(
                new HighlightBuilder.Field("title"), new HighlightBuilder.Field("description"))
            .withHighlightBuilder(
                new HighlightBuilder().preTags("<span style=\"color: red\">").postTags("</span>"))
            .build();
      //查询
      SearchHits<Blog> search = restTemplate.search(searchQuery, Blog.class);
      //得到查询返回的内容
      List<SearchHit<Blog>> searchHits = search.getSearchHits();
      //设置一个最后需要返回的实体类集合
      List<Blog> blogs = new ArrayList<>();
      //遍历返回的内容进行处理
      for(SearchHit<Blog> searchHit:searchHits){
          //高亮的内容
          Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
          //将高亮的内容填充到content中
          searchHit.getContent().setTitle(highlightFields.get("title")==null ? searchHit.getContent().getTitle():highlightFields.get("title").get(0));
          searchHit.getContent().setDescription(highlightFields.get("description")==null ? searchHit.getContent().getDescription():highlightFields.get("description").get(0));
          //放到实体类中
          blogs.add(searchHit.getContent());
      }
      return Result.succ(blogs);
  }


}


