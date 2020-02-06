package com.java1234.controller.user;

import com.java1234.common.DataGridView;
import com.java1234.entity.Article;
import com.java1234.entity.User;
import com.java1234.entity.UserDownload;
import com.java1234.lucene.ArticleIndex;
import com.java1234.service.ArticleService;
import com.java1234.service.CommentService;
import com.java1234.service.UserDownloadService;
import com.java1234.service.UserService;
import com.java1234.util.DateUtil;
import com.java1234.util.RedisUtil;
import com.java1234.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发布帖子控制器
 * @Date 2020/2/1 12:10
 * @Author JianHui
 */
@Controller
@RequestMapping("/user/article")
public class ArticleUserController {

    @Autowired
    private ArticleService articleService;

    @Value("${articleImageFilePath}")
    private String articleImageFilePath;

    @Autowired
    private UserDownloadService userDownloadService;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleIndex articleIndex;


    @Autowired
    private CommentService commentService;

    @Autowired
    private RedisUtil<Article> redisUtil;



    /**
     * layui编辑器地址
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping("uploadImage")
    @ResponseBody
    public DataGridView uploadImage(MultipartFile file)throws Exception{
        DataGridView dataGridView = null;
        if (!file.isEmpty()){
            String filename = file.getOriginalFilename(); //abc.jpg
            String suffix = filename.substring(filename.lastIndexOf(".")); //后缀 如abc.jpg,就是jpg
            String newFileName = DateUtil.getCurrentDateStr() + suffix;  //新文件名
            FileUtils.copyInputStreamToFile(file.getInputStream(),new File(articleImageFilePath+DateUtil.getCurrentDatePath()+newFileName));
            Map<String,Object> map= new HashMap<>();
            System.out.println("newFileName："+newFileName);
            map.put("src","/image/"+DateUtil.getCurrentDatePath()+newFileName);
            map.put("title",newFileName);
            dataGridView = new DataGridView(0, "上传成功", map);
        }
        return dataGridView;
    }

    /**
     * 跳转到发布帖子页面
     * @return
     */
    @RequestMapping("toPublishArticlePage")
    public ModelAndView toPublishArticlePage(){
        ModelAndView andView = new ModelAndView();
        andView.addObject("title","发布帖子页面");
        andView.setViewName("user/publishArticle");
        return andView;
    }

    /**
     * 跳转到发布帖子页面
     * @return
     */
    @RequestMapping("toModifyArticlePage/{id}")
    public ModelAndView toModifyArticlePage(@PathVariable("id")Integer id){
        ModelAndView andView = new ModelAndView();
        andView.addObject("title","修改帖子页面");
        andView.setViewName("user/modifyArticle");
        andView.addObject("article",articleService.get(id));
        return andView;
    }

    /**
     * 跳转到管理帖子页面
     * @return
     */
    @RequestMapping("toArticleManagePage")
    public ModelAndView toArticleManagePage(){
        ModelAndView andView = new ModelAndView();
        andView.addObject("title","管理帖子页面");
        andView.setViewName("user/articleManage");
        return andView;
    }

    /**
     * 添加帖子
     * @param article
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    public ModelAndView add(Article article, HttpSession session)throws Exception{
        ModelAndView andView = new ModelAndView();
        User currentUser = (User) session.getAttribute("currentUser");
        article.setPublishDate(new Date());
        article.setUser(currentUser);
        article.setState(1);  //未审核状态
        article.setView(StringUtil.randomInteger());
        articleService.save(article);
        andView.addObject("title","发布帖子成功页面");
        andView.setViewName("user/publishArticleSuccess");
        return andView;
    }


    /**
     * 添加帖子
     * @param article
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    public ModelAndView update(Article article )throws Exception{
        Article oldArticle = articleService.get(article.getId());
        oldArticle.setName(article.getName());
        oldArticle.setArcType(article.getArcType());
        oldArticle.setContent(article.getContent());
        oldArticle.setDownload1(article.getDownload1());
        oldArticle.setPassword1(article.getPassword1());
        oldArticle.setPoints(article.getPoints());
        oldArticle.setPublishDate(new Date());
        if (oldArticle.getState() == 3){  //帖子未通过，用户点击修改，重新审核
            oldArticle.setState(1);
        }
        articleService.save(oldArticle);
        if (oldArticle.getState()==2){ //审核通过
            articleIndex.updateIndex(oldArticle);
            if(redisUtil.hasKey("article_"+oldArticle.getId())){
                redisUtil.del("article_"+oldArticle.getId());
            }
        }
        ModelAndView andView = new ModelAndView();
        andView.addObject("title","修改帖子成功页面");
        andView.setViewName("user/modifyArticleSuccess");
        return andView;
    }

    /**
     * 根据资源分页查询帖子信息
     * @param article
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public DataGridView list(Article s_article, @RequestParam(value = "page" ,required = false)Integer page
            ,@RequestParam(value = "limit" ,required = false)Integer limit,HttpSession session){
        User currentUser = (User) session.getAttribute("currentUser");
        s_article.setUser(currentUser);
        List<Article> articleList = articleService.list(s_article, page, limit, Sort.Direction.DESC, "publishDate");
        Long total = articleService.getTotal(s_article);
        return new DataGridView(0,total,articleList);
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("delete")
    public DataGridView  delete(Integer id) throws Exception{
        articleService.delete(id);
        userDownloadService.deleteByArticleId(id);
        commentService.deleteByArticleId(id);
        articleIndex.delete(String.valueOf(id));
        if(redisUtil.hasKey("article_"+id)){
            redisUtil.del("article_"+id);
        }
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
            if(redisUtil.hasKey("article_"+idStr[i])){
                redisUtil.del("article_"+idStr[i]);
            }
            articleService.delete(Integer.parseInt(idStr[i]));
            articleIndex.delete(String.valueOf(idStr[i]));
            commentService.deleteByArticleId(Integer.parseInt(idStr[i]));
            articleIndex.delete(String.valueOf(idStr[i]));
        }
        return new DataGridView(true);
    }

    /**
     * 跳转到资源下载页面
     * @param id
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("toDownLoadPage/{id}")
    public ModelAndView toDownLoadPage(@PathVariable("id")Integer id,HttpSession session)throws Exception{
        ModelAndView andView = new ModelAndView();
        UserDownload userDownload=new UserDownload();
        Article article = articleService.get(id);
        User currentUser = (User) session.getAttribute("currentUser");

        boolean isDownload=false;
        Integer count = userDownloadService.getCountByUserIdAndArticleId(currentUser.getId(), article.getId());
        if (count>0){
            isDownload=true;
        }//否则就算没下载过
        if (!isDownload) {
            if (currentUser.getPoints() - article.getPoints() < 0) {  //用户积分不够
                return null;
            }


            //扣积分
            currentUser.setPoints(currentUser.getPoints() - article.getPoints());
            userService.save(currentUser);

            //给分享人加积分
            User articleUser = article.getUser();
            articleUser.setPoints(articleUser.getPoints() + article.getPoints());
            userService.save(articleUser);
        }
            //保存用户下载信息
            userDownload.setArticle(article);
            userDownload.setUser(currentUser);
            userDownload.setDownloadDate(new Date());
            userDownloadService.save(userDownload);
            andView.addObject("article",articleService.get(id));
            andView.setViewName("user/downloadPage");
        return andView;
    }


    /**
     * 跳转到vip下载资源下载页面
     * @param id
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("toVipDownLoadPage/{id}")
    public ModelAndView toVipDownLoadPage(@PathVariable("id")Integer id,HttpSession session)throws Exception{
        ModelAndView andView = new ModelAndView();
        UserDownload userDownload=new UserDownload();
        Article article = articleService.get(id);
        User currentUser = (User) session.getAttribute("currentUser");

        if (!currentUser.isVip()){ //判断是否是vip
            return  null;
        }
        boolean isDownload=false;
        Integer count = userDownloadService.getCountByUserIdAndArticleId(currentUser.getId(), article.getId());
        if (count>0){
            isDownload=true;
        }//否则就算没下载过
        if (!isDownload) {
            //保存用户下载信息
            userDownload.setArticle(article);
            userDownload.setUser(currentUser);
            userDownload.setDownloadDate(new Date());
            userDownloadService.save(userDownload);
        }
        andView.addObject("article",articleService.get(id));
        andView.setViewName("user/downloadPage");
        return andView;
    }

}
