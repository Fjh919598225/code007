package com.java1234.controller.user;

import com.java1234.common.DataGridView;
import com.java1234.entity.Article;
import com.java1234.entity.Comment;
import com.java1234.entity.Comment;
import com.java1234.entity.User;
import com.java1234.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * 用户评论控制器
 * @Date 2020/2/3 17:33
 * @Author JianHui
 */
@Controller
@RequestMapping("/user/comment")
public class CommentUserController {

    @Autowired
    private CommentService commentService;

    /**
     * 保存评论信息
     * @param comment
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping("save")
    public DataGridView save(Comment comment, HttpSession session){
        comment.setCommentDate(new Date());
        comment.setState(0);
        comment.setUser((User) session.getAttribute("currentUser"));
        commentService.save(comment);
        return new DataGridView(true);
    }



    /**
     * 跳转到评论管理页面
     * @return
     */
    @RequestMapping("toCommentManagePage")
    public ModelAndView toCommentManagePage(){
        ModelAndView andView = new ModelAndView();
        andView.addObject("title","评论管理页面");
        andView.setViewName("user/commentManage");
        return andView;
    }

    /**
     * 根据资源分页查询评论信息
     * @param article
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public DataGridView list(Comment comment, @RequestParam(value = "page" ,required = false)Integer page
            , @RequestParam(value = "limit" ,required = false)Integer limit, HttpSession session){
        User currentUser = (User) session.getAttribute("currentUser");
        comment.setUser(currentUser);
        Article article = new Article();
        article.setUser((User) session.getAttribute("currentUser"));
        comment.setArticle(article);
        comment.setState(1);
        List<Comment> commentList = commentService.list(comment, page, limit, Sort.Direction.DESC, "commentDate");
        Long total = commentService.getTotal(comment);
        return new DataGridView(0,total,commentList);
    }

    /**
     * 根据删除评论
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("delete")
    public DataGridView  delete(Integer id) throws Exception{
        commentService.delete(id);
        return new DataGridView(true);
    }

    /**
     * 删除多条帖子
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("deleteSelected")
    public  DataGridView  deleteSelected(String ids) throws Exception{
        String[] idStr = ids.split(",");
        for (int i = 0; i < idStr.length; i++) {
            commentService.delete(Integer.parseInt(idStr[i]));
        }
        return new DataGridView(true);
    }

}
