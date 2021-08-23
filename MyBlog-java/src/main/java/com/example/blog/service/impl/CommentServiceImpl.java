package com.example.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blog.entity.Blog;
import com.example.blog.entity.Comment;
import com.example.blog.mapper.BlogMapper;
import com.example.blog.mapper.CommentMapper;
import com.example.blog.service.CommentService;
import com.example.blog.utils.RedisUtil;
import com.example.blog.vo.Const;
import com.example.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sy
 * @since 2021-08-15
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    CommentMapper commentMapper ;
    @Autowired
    BlogMapper blogMapper;
    @Autowired
    RedisUtil redisUtil;
    QueryWrapper qw =new QueryWrapper<Comment>();
    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();
    //存放要删除的评论id
    private List<Integer> deleteListId = new ArrayList<>();

    /**
     * 保存评论
     * @param comment
     * @return
     */
    @Override
    public Result saveComment(Comment comment) {
        commentMapper.insert(comment);
        if (comment.getBlogId()==-1){//如果是在留言板留言，则留言数+1
            redisUtil.incr(Const.MESSAGECOUNT,1);//redis中博客数量+1
        }else {//不是在留言板留言，则相关博客评论数加1
            Blog blog = blogMapper.selectById(comment.getBlogId());//根据评论携带的博客id查找博客
            blog.setCommentCount(blog.getCommentCount()+1);//博客评论数+1
            blogMapper.updateById(blog);
        }
        return Result.succ("评论成功");
    }

    /**
     * 分页查询
     * @param current
     * @param size
     * @return
     */
    @Override
    public Result getCommentByPage(Long current, Long size) {
        Page<Comment> page = new Page<>(current,size);
        qw.orderByDesc("create_time");
        Page commentPage = commentMapper.selectPage(page, qw);
        qw.clear();
        return Result.succ(commentPage);
    }

    /**
     * 根据id删除评论，并修改相关博客的评论数
     * @param id
     * @return
     */
    @Override
    public Result deleteCommentById(Integer id) {
        //查询子评论，（删除评论时，需要把它的子评论也全部删除）
        qw.eq("parent_comment_id",id);
        List<Comment> comments = commentMapper.selectList(qw);
        Comment comment = commentMapper.selectById(id);//根据id查询评论信息
        deleteListId.add(id);//将父评论id存放到删除列表中
        qw.clear();
        getDeleteListId(comments);
        int sum = commentMapper.deleteBatchIds(deleteListId);//删除评论并记录删除的评论数
        deleteListId.clear();//清空删除列表

        if (comment.getBlogId() == -1) {//如果删除的是留言板中的留言，则网站留言数减少删除条数
            redisUtil.decr(Const.MESSAGECOUNT,sum);
        }else{//否则相关博客的评论数减少
           Blog blog = blogMapper.selectById(comment.getBlogId()); // 根据评论信息中的博客id查询博客
           blog.setCommentCount(blog.getCommentCount() - sum); // 修改博客的评论数量
           blogMapper.updateById(blog);
    }
        return Result.succ("成功删除"+sum+"条评论");
    }

//获取要删除的评论id列表
   public void getDeleteListId(List<Comment> comments){
        if(!comments.isEmpty()){
      for (Comment comment : comments) {
          Integer id = comment.getId();
          qw.eq("parent_comment_id",id);
          //存入要删除的评论id
          deleteListId.add(id);
          List<Comment> comments1 = commentMapper.selectList(qw);
          getDeleteListId(comments1);//递归查找需要删除的评论id
          qw.clear();
      }
        }
    }

    /**
     * 删除博客下的全部评论
     * @param id
     * @return
     */
    @Override
    public void deleteCommentByBlogId(Integer id) {
        qw.eq("blog_id",id);
        commentMapper.delete(qw);
        qw.clear();
    }

    /**
     * @Description: 查询评论
     * @Param: blogId：博客id
     * @Return: 评论消息
     */
    @Override
    public Result getCommentById(Integer blogId) {
        //查询出父节点
        qw.eq("blog_id",blogId);
        qw.eq("parent_comment_id",Integer.parseInt("-1"));
        qw.orderByDesc("create_time");
        List<Comment> comments = commentMapper.selectList(qw);
        qw.clear();
        for(Comment comment : comments){
            //子评论的父id为当前评论id
            Integer parentId = comment.getId();
            String parentNickname1 = comment.getNickname();
            qw.eq("blog_id",blogId);
            qw.eq("parent_comment_id",parentId);
            qw.orderByDesc("create_time");
            List<Comment> childComments = commentMapper.selectList(qw);
            qw.clear();
            //查询出子评论
            combineChildren( childComments, parentNickname1);
            comment.setReplyComments(tempReplys);
            tempReplys = new ArrayList<>();
        }
        return Result.succ(comments);
    }


    /**
     * @Description: 查询出子评论
     * @Auther: SY
     * @Param: childComments：所有子评论
     * @Param: parentNickname1：父评论姓名
     * @Return:
     */
    private void combineChildren( List<Comment> childComments, String parentNickname1) {
        //判断是否有一级子评论
        if(childComments.size() > 0){
            //循环找出子评论的id
            for(Comment childComment : childComments){
                String parentNickname = childComment.getNickname();
                childComment.setParentNickname(parentNickname1);
                tempReplys.add(childComment);
                Integer parentId = childComment.getId();
                //查询出子二级评论
                recursively( parentId, parentNickname);
            }
        }
    }

    /**
     * @Description: 循环迭代找出子集回复
     * @Auther: SY
     * @Param: chileId:子评论id
     * @Param: parentNickname1:子评论姓名
     * @Return:
     */
    private void recursively( Integer parentId, String parentNickname1) {
        //根据子一级评论的id找到子二级评论
        qw.eq("parent_comment_id",parentId);
        qw.orderByDesc("create_time");
        List<Comment> replayComments = commentMapper.selectList(qw);

        if(replayComments.size() > 0){
            for(Comment replayComment : replayComments){
                String parentNickname = replayComment.getNickname();
                replayComment.setParentNickname(parentNickname1);
                Integer replayId = replayComment.getId();
                tempReplys.add(replayComment);
                recursively(replayId,parentNickname);//递归调用，查询子二级评论是否还有子评论
            }
        }
        qw.clear();
    }

}
