package com.java1234.controller.admin;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**首页或者跳转url控制器
 * @Date 2020/2/1 21:54
 * @Author JianHui
 */
@Controller
public class IndexAdminController {

    /**
     * 跳转到管理员主页面
     * @return
     */
    @RequiresPermissions(value = {"进入管理员主页"})
    @RequestMapping("/toAdminUserCenterPage")
    public ModelAndView toAdminUserCenterPage(){
        ModelAndView andView = new ModelAndView();
        andView.addObject("title","管理员主页面");
        andView.setViewName("admin/adminUserCenter");
        return andView;
    }
}
