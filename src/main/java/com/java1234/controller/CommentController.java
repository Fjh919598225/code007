package com.java1234.controller;

import com.java1234.entity.Comment;
import com.java1234.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 评论控制器
 * @Date 2020/2/3 18:10
 * @Author JianHui
 */
@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 分页查询评论帖子信息
     * @param comment
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public List<Comment> list(Comment comment, @RequestParam(value = "page",required = false)Integer page){
        comment.setState(1);
        List<Comment> commentList = commentService.list(comment, page, 6, Sort.Direction.DESC, "commentDate");
        return commentList;
    }
}
