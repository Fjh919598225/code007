package com.java1234.controller.user;

import com.java1234.entity.Article;
import com.java1234.entity.User;
import com.java1234.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 用户页面控制器
 * @Date 2020/1/30 14:31
 * @Author JianHui
 */
@Controller
public class IndexUserController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping("toUserCenterPage")
    public ModelAndView toUserCenterPage(HttpSession session){
        User currentUser = (User) session.getAttribute("currentUser");
        Article s_article = new Article();
        s_article.setUser(currentUser);
        s_article.setUseful(false);
        Long total = articleService.getTotal(s_article);
        ModelAndView andView = new ModelAndView();
        andView.addObject("title","用户中心页面");
        session.setAttribute("unUsefulArticleCount",total);
        andView.setViewName("user/userCenter");
        return andView;
    }

}
