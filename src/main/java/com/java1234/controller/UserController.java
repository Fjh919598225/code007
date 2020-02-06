package com.java1234.controller;

import com.google.gson.Gson;
import com.java1234.common.DataGridView;
import com.java1234.entity.User;
import com.java1234.entity.VaptchaMessage;
import com.java1234.service.MessageService;
import com.java1234.service.UserService;
import com.java1234.util.CryptographyUtil;
import com.java1234.util.DateUtil;
import com.java1234.util.RedisUtil;
import com.java1234.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.xml.crypto.Data;
import java.io.File;
import java.util.*;

/**
 * 用户控制价
 * @Date 2020/1/26 13:29
 * @Author JianHui
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Resource
    private JavaMailSender mailSender;

    @Value("${userImageFilePath}")
    private String userImageFilePath;

    @Autowired
    private MessageService messageService;

    @Autowired
    private RedisUtil<Integer> redisUtil;

    /**
     * 用户登录请求
     * @param user
     * @param bindingResult
     * @param vaptcha_token
     * @param request
     * @return
     */
    @RequestMapping("login")
    @ResponseBody
    public Map<String,Object> login(@Valid User user, BindingResult bindingResult
           ,HttpServletRequest request) throws Exception {
        Map<String,Object> map= new HashMap<>();
        if (StringUtil.isEmpty(user.getUserName().trim())){
            map.put("success",false);
            map.put("errorInfo","请输入用户名！");
        }else if (StringUtil.isEmpty(user.getPassword().trim())) {
            map.put("success",false);
            map.put("errorInfo","请输入密码！");
        }else {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token =new UsernamePasswordToken(user.getUserName(),CryptographyUtil.md5(user.getPassword(),CryptographyUtil.SALT));
            try {
                subject.login(token);
                String userName = (String) SecurityUtils.getSubject().getPrincipal();
                User currentUser = userService.findByUserName(userName);
                if (currentUser.isOff()){
                    map.put("success",false);
                    map.put("errorInfo","该用户已经被封禁，请联系管理员！");
                    subject.logout();
                }else {
                    Integer count = messageService.getCountByUserId(currentUser.getId());
                    currentUser.setMessageCount(count);
                    request.getSession().setAttribute("currentUser",currentUser);
                    map.put("success",true);
                }
            }catch (Exception e){
                e.printStackTrace();
                map.put("success",false);
                map.put("errorInfo","用户名或者密码错误！");
            }
        }
        return map;
    }

    /**
     * 发送邮件
     * @param email
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("sendEmail")
    public Map<String,Object> sendEmail(String email,HttpSession session)throws Exception{
        Map<String,Object> resulMap=new HashMap<String,Object>();
        if(StringUtil.isEmpty(email)){
            resulMap.put("success", false);
            resulMap.put("errorInfo", "邮件不不能为空！");
            return resulMap;
        }
        User u = userService.findByEmail(email);
        if(u==null){
            resulMap.put("success", false);
            resulMap.put("errorInfo", "这个邮件不存在！");
            return resulMap;
        }
        String mailCode = StringUtil.genSixRandomNum();
        System.out.println("mailCode:"+mailCode);
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("919598225@qq.com"); // 发件人
        message.setTo(email);
        message.setSubject("java1234下载站点-用户找回密码"); // 主题
        message.setText("验证码："+mailCode);
        mailSender.send(message);

        // 验证码存到session中
        session.setAttribute("mailCode", mailCode);
        session.setAttribute("userId", u.getId());
        resulMap.put("success", true);
        return resulMap;
    }

    /**
     * 邮件验证码判断 重置密码
     * @param yzm
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("checkYzm")
    public Map<String,Object> checkYzm(String yzm,HttpSession session)throws Exception{
        Map<String,Object> resulMap=new HashMap<String,Object>();
        if(StringUtil.isEmpty(yzm)){
            resulMap.put("success", false);
            resulMap.put("errorInfo", "验证码不能为空！");
            return resulMap;
        }
        String mailCode = (String) session.getAttribute("mailCode");
        Integer userId = (Integer) session.getAttribute("userId");
        if (!yzm.equals(mailCode)){
            resulMap.put("success", false);
            resulMap.put("errorInfo", "验证码错误！");
            return resulMap;
        }
        User user = userService.getById(userId);
        user.setPassword(CryptographyUtil.md5("123456",CryptographyUtil.SALT));
        userService.save(user);
        resulMap.put("success", true);
        return resulMap;
    }

    @RequestMapping("register")
    @ResponseBody
    public Map<String,Object> register(@Valid User user, BindingResult bindingResult
           ,HttpServletRequest request) throws Exception {
        Map<String,Object> map= new HashMap<>();
        if (bindingResult.hasErrors()){
            map.put("success",false);
            map.put("errorInfo",bindingResult.getFieldError().getDefaultMessage());
        }else if (userService.findByUserName(user.getUserName())!=null){
            map.put("success",false);
            map.put("errorInfo","用户名已被注册，请更换！");
        }else if (userService.findByEmail(user.getEmail())!=null){
            map.put("success",false);
            map.put("errorInfo","邮箱已被注册，请更换！");
        } else {
            user.setPassword(CryptographyUtil.md5(user.getPassword(),CryptographyUtil.SALT));
            user.setRegistryDate(new Date());
            user.setImageName("default.jpg");
            userService.save(user);
            map.put("success",true);
        }
        return map;
    }

    /**
     * 上传头像
     * @param file
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("uploadImage")
    public DataGridView uploadImage(MultipartFile file, HttpSession session) throws Exception {
        DataGridView dataGridView = null;
        if (!file.isEmpty()){
            String filename = file.getOriginalFilename(); //abc.jpg
            String suffix = filename.substring(filename.lastIndexOf(".")); //后缀 如abc.jpg,就是jpg
            String newFileName = DateUtil.getCurrentDateStr() + suffix;  //新文件名
            FileUtils.copyInputStreamToFile(file.getInputStream(),new File(userImageFilePath+newFileName));
            Map<String,Object> map= new HashMap<>();
            map.put("src","/userImage/"+newFileName);
            map.put("title",newFileName);
            dataGridView = new DataGridView(0, "上传成功", map);

            User currentUser = (User) session.getAttribute("currentUser");
            currentUser.setImageName(newFileName);
            userService.save(currentUser);
            session.setAttribute("currentUser",currentUser);
            System.out.println("执行完了");
        }
        return dataGridView;
    }

    /**
     * 判断当前用户是否是VIP用户
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping("isVip")
    public boolean isVip(HttpSession session){
        User currentUser = (User) session.getAttribute("currentUser");
        return currentUser.isVip();
    }

    /**
     * 用户签到
     * @param session
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("sign")
    public Map<String,Object> sign(HttpSession session,HttpServletRequest request)throws Exception{
        Map<String,Object> map=new HashMap<>();
        User currentUser= (User) session.getAttribute("currentUser");
        if (currentUser==null){
            map.put("success",false);
            map.put("errorInfo","请先登录，再签到！");
            return map;
        }else if (currentUser.isSign()){
            map.put("success",false);
            map.put("errorInfo","您已签到！");
            return map;
        }
        ServletContext application = request.getServletContext();
        Integer signTotal = (Integer) redisUtil.get("signTotal");
        redisUtil.set("signTotal",signTotal+1);
        application.setAttribute("signTotal",signTotal+1);

        //更新到数据库
        User user = userService.getById(currentUser.getId());
        user.setSign(true);
        user.setSignTime(new Date());
        user.setSignSort(signTotal+1);
        user.setPoints(user.getPoints()+3);
        userService.save(user);
        session.setAttribute("currentUser",currentUser);
        map.put("success",true);
        return map;

    }

}
