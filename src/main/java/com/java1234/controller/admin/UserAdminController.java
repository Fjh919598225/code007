package com.java1234.controller.admin;

import com.java1234.common.DataGridView;
import com.java1234.entity.User;
import com.java1234.service.UserService;
import com.java1234.util.CryptographyUtil;
import org.apache.shiro.SecurityUtils;
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
 * 管理用户控制器
 * @Date 2020/2/2 15:59
 * @Author JianHui
 */
@Controller
@RequestMapping("/admin/user")
public class UserAdminController {

    @Autowired
    private UserService userService;


    /**
     * 根据条件分页查询用户
     * @param user
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    @RequiresPermissions(value = {"分页查询用户信息"})
    public DataGridView list(User user,@RequestParam(value = "page",required = false)Integer page
            , @RequestParam(value = "limit",required = false)Integer limit){
        List<User> userList = userService.list(user,page, limit, Sort.Direction.DESC, "registryDate");
        Long total = userService.getTotal(user);
        return new DataGridView(0,total,userList);
    }

    /**
     * 重置密码
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("resetPassword")
    @RequiresPermissions(value = {"重置用户密码"})
    public DataGridView resetPassword(Integer id)throws Exception{
        User user = userService.getById(id);
        user.setPassword(CryptographyUtil.md5("123456",CryptographyUtil.SALT));
        userService.save(user);
        return new DataGridView(true);
    }

    @ResponseBody
    @RequestMapping("addPoints")
    @RequiresPermissions(value = {"用户积分充值"})
    public DataGridView addPoints(User user){
        User oldUser = userService.getById(user.getId());
        oldUser.setPoints(oldUser.getPoints()+user.getPoints());
        userService.save(oldUser);
        return new DataGridView(true);
    }

    /**
     * 修改用户VIP状态
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping("updateVipState")
    @RequiresPermissions(value = {"修改用户VIP状态"})
    public DataGridView updateVipState(User user){
        User oldUser = userService.getById(user.getId());
        oldUser.setVip(user.isVip());
        userService.save(oldUser);
        return new DataGridView(true);
    }

    /**
     * 修改用户状态
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping("updateUserState")
    @RequiresPermissions(value = {"修改用户状态"})
    public DataGridView updateUserState(User user){
        User oldUser = userService.getById(user.getId());
        oldUser.setOff(user.isOff());
        userService.save(oldUser);
        return new DataGridView(true);
    }


    /**
     *
     * @param oldpassword
     * @param password
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping("modifyPassword")
    @RequiresPermissions(value = {"修改管理员密码"})
    public DataGridView modifyPassword(String oldpassword, String password, HttpSession session){
        User currentUser = (User) session.getAttribute("currentUser");
        if (!currentUser.getPassword().equals(CryptographyUtil.md5(oldpassword,CryptographyUtil.SALT))){
            return new DataGridView(false);
        }else {
            User user = userService.getById(currentUser.getId());
            user.setPassword(CryptographyUtil.md5(password,CryptographyUtil.SALT));
            userService.save(user);
            return new DataGridView(true);
        }
    }

    /**
     * 管理员退出
     * @param session
     * @return
     */
    @RequestMapping("logout")
    @RequiresPermissions(value = {"安全退出"})
    public String logout(HttpSession session){
        SecurityUtils.getSubject().logout();
        return "redirect:/adminLogin.html";
    }
}
