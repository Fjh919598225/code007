package com.java1234.controller.user;

import com.java1234.entity.UserDownload;
import com.java1234.entity.User;
import com.java1234.service.UserDownloadService;
import com.java1234.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 用户-用户下载k控制器
 * @Date 2020/2/3 16:00
 * @Author JianHui
 */
@Controller
@RequestMapping("/user/userDownload")
public class UserDownloadUserController {

    @Autowired
    private UserDownloadService userDownloadService;

    /**
     * 判断资源是否下载过
     * @param id
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("exist")
    public boolean exist(Integer id, HttpSession session)throws Exception{
        User currentUser = (User) session.getAttribute("currentUser");
        Integer count = userDownloadService.getCountByUserIdAndArticleId(currentUser.getId(), id);
        return count>0?true:false;
    }

    /**
     * 判断用户积分是否足够下载这个资源
     * @param points
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("enough")
    public boolean enough(Integer points,HttpSession session)throws Exception{
        User currentUser = (User) session.getAttribute("currentUser");
        return currentUser.getPoints()>=points?true:false;
    }



    /**
     * 分页查询消息帖子
     * @param session
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("/list/{page}")
    public ModelAndView list(HttpSession session, @PathVariable(value="page",required=false)Integer page)throws Exception{
        ModelAndView mav=new ModelAndView();
        User user=(User)session.getAttribute("currentUser");
        UserDownload s_userDownload=new UserDownload();
        s_userDownload.setUser(user);
        List<UserDownload> userDownloadList = userDownloadService.list(s_userDownload, page, 10, Sort.Direction.DESC, "downloadDate");
        Long total = userDownloadService.getTotal(s_userDownload);
        mav.addObject("userDownloadList", userDownloadList);
        mav.addObject("pageCode", PageUtil.genPagination("/user/userDownload/list", total, page, 10, ""));
        mav.addObject("title", "系统消息页面");
        mav.setViewName("user/listUserDownload");
        return mav;
    }
}
