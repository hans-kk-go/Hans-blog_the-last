package com.hans.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hans.commen.ResponseResult;
import com.hans.dao.CommentDao;
import com.hans.entity.Comment;
import com.hans.service.CommentService;
import com.hans.service.UserService;
import com.hans.vo.CommentVo;
import com.hans.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-03-28 22:16:04
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {
    @Autowired
    private UserService userService;


    public List<CommentVo> getCommentVo(List<CommentVo> list){
        List<CommentVo> list1 = list.stream()
                .peek(one -> {
                    if (one.getToCommentUserId() != -1) {
                        one.setToCommentUserName(userService.getById(one.getToCommentUserId()).getUserName());
                    }})
                .peek(one -> one.setUsername(userService.getById(one.getCreateBy()).getNickName()))
                .collect(Collectors.toList());
        return list1;
    }

    @Override
    public ResponseResult getCommentList(String s, Long articleId, Integer pageNum, Integer pageSize) {
//
        LambdaQueryWrapper<Comment> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //查询对应文章的根评论
        //对articleId进行判断
        if (s.equals("0")){
            articleLambdaQueryWrapper.eq(Comment::getArticleId,articleId);
        }

        articleLambdaQueryWrapper.eq(Comment::getType,s);



        //根评论 rootId为-1
        articleLambdaQueryWrapper.eq(Comment::getRootId,"-1");
//        //分页查询
        Page<Comment> commentPage = new Page<>(pageNum,pageSize);
        List<Comment> commentList = page(commentPage, articleLambdaQueryWrapper).getRecords();
        List<CommentVo> commentVos = BeanUtil.copyToList(commentList, CommentVo.class);

        //查得根评论
        List<CommentVo> commentVoss = getCommentVo(commentVos);
        //根据根评论id查子评论
        List<CommentVo> allCommentVo = getCommentVo(BeanUtil.copyToList(list(), CommentVo.class));

        //给子评论赋值
        List<CommentVo> commentVos1 = commentVoss.stream()
                .peek(one -> one.setChildren(allCommentVo.stream().filter(o -> o.getRootId().equals(one.getRootId() + 2))
                        .sorted(Comparator.comparing(CommentVo::getCreateTime).reversed()).collect(Collectors.toList())))
                .collect(Collectors.toList());


        PageVo pageVo = new PageVo(commentPage.getPages(),commentVos1);
        return ResponseResult.ok(pageVo);

    }

    @Override
    public ResponseResult addComment(Comment comment) {
        if (StrUtil.isBlankIfStr(comment)){
            return ResponseResult.fail(401,"添加内容为空");
        }
//        comment.setCreateBy(SecurityUtils.getUser().getId());
        save(comment);
        return ResponseResult.ok("回复成功",comment);
    }

}
