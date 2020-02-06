package com.java1234.controller.admin;

import com.java1234.common.DataGridView;
import com.java1234.entity.Article;
import com.java1234.entity.Comment;
import com.java1234.entity.User;
import com.java1234.service.CommentService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 管理员-评论控制器
 * @Date 2020/2/4 15:46
 * @Author JianHui
 */
@Controller
@RequestMapping("/admin/comment")
public class CommentAdminController {

    @Autowired
    private CommentService commentService;

    /**
     * 根据资源分页查询评论信息
     * @param article
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    @RequiresPermissions(value = {"分页查询评论信息"})
    public DataGridView list(Comment comment, @RequestParam(value = "page" ,required = false)Integer page
            , @RequestParam(value = "limit" ,required = false)Integer limit){
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
    @RequiresPermissions(value = {"分页查询评论信息"})
    public DataGridView delete(Integer id) throws Exception{
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
    @RequiresPermissions(value = {"删除评论"})
    public  DataGridView  deleteSelected(String ids) throws Exception{
        String[] idStr = ids.split(",");
        for (int i = 0; i < idStr.length; i++) {
            commentService.delete(Integer.parseInt(idStr[i]));
        }
        return new DataGridView(true);
    }

    /**
     * 修改评论
     * @param id
     * @param state
     * @return
     */
    @ResponseBody
    @RequestMapping("updateState")
    @RequiresPermissions(value = {"修改评论状态"})
    public DataGridView updateState(Integer id,Boolean state){
        Comment comment = commentService.get(id);
        comment.setState(state==true?1:2);
        commentService.save(comment);
        return new DataGridView(true);
    }
}
