package com.java1234.controller.admin;

import com.java1234.common.DataGridView;
import com.java1234.entity.ArcType;
import com.java1234.entity.Article;
import com.java1234.entity.Message;
import com.java1234.lucene.ArticleIndex;
import com.java1234.service.ArticleService;
import com.java1234.service.CommentService;
import com.java1234.service.MessageService;
import com.java1234.service.UserDownloadService;
import com.java1234.util.DateUtil;
import com.java1234.util.RedisUtil;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date 2020/2/2 17:01
 * @Author JianHui
 */
@Controller
@RequestMapping("/admin/article")
public class ArticleAdminController {

    @Autowired
    private ArticleService articleService;


    @Value("${articleImageFilePath}")
    private String articleImageFilePath;

    @Autowired
    private ArticleIndex articleIndex;

    @Autowired
    private UserDownloadService userDownloadService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private RedisUtil<Article> redisUtil;

    @ResponseBody
    @RequestMapping("getAllIndex")
    @RequiresPermissions(value = {"生成所有帖子索引"})
    public boolean getAllIndex(){
        List<Article> articleList = articleService.getArticleNoIndex();
        for (Article article:articleList) {
            if (!articleIndex.addIndex(article)){
                return false;
            }else {  //生成索引成功，更改索引状态
                articleService.updateArticleIndex(article.getId());
            }
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("list")
    @RequiresPermissions(value = {"分页查询资源帖子信息"})
    public DataGridView list(Article article, @RequestParam(value = "page",required = false)Integer page
            , @RequestParam(value = "limit",required = false)Integer limit){
        List<Article> articleList = articleService.list(article,page, limit, Sort.Direction.DESC, "publishDate");
        Long total = articleService.getTotal(article);
        return new DataGridView(0,total,articleList);
    }


    /**
     * 跳转到帖子审核页面
     * @param id
     * @return
     */
    @RequiresPermissions(value = {"跳转到帖子审核页面"})
    @RequestMapping("/toReViewArticlePage/{id}")
    public ModelAndView toReViewArticlePage(@PathVariable("id") Integer id){
        ModelAndView andView = new ModelAndView();
        andView.addObject("article",articleService.get(id));
        andView.addObject("title","审核帖子页面");
        andView.setViewName("admin/reviewArticle");

        return andView;
    }

    /**
     * 跳转到帖子修改页面
     * @param id
     * @return
     */
    @RequiresPermissions(value = {"跳转到修改帖子页面"})
    @RequestMapping("/toModifyArticlePage/{id}")
    public ModelAndView toModifyArticlePage(@PathVariable("id") Integer id){
        ModelAndView andView = new ModelAndView();
        andView.addObject("article",articleService.get(id));
        andView.addObject("title","修改帖子页面");
        andView.setViewName("admin/modifyArticle");

        return andView;
    }

    @ResponseBody
    @RequestMapping("updateState")
    @RequiresPermissions(value = {"修改状态"})
    public DataGridView updateState(Article article)throws Exception{
        Article oldArticle = articleService.get(article.getId());
        Message message = new Message();
        message.setUser(oldArticle.getUser());
        message.setPublishDate(new Date());

        if (article.getState()==2){
            oldArticle.setState(2);
            articleIndex.addIndex(oldArticle);
            message.setContent("【审核通过】您发布的【"+oldArticle.getName()+"】审核成功！");
            if(redisUtil.hasKey("article_"+oldArticle.getId())){
                redisUtil.del("article_"+oldArticle.getId());
            }

        }else if (article.getState()==3){
            oldArticle.setState(article.getState());
            oldArticle.setReason(article.getReason());
            message.setContent("【审核不通过】您发布的【"+oldArticle.getName()+"】审核不通过！原因是："+oldArticle.getReason()+"");
        }
        articleService.save(oldArticle);
        messageService.save(message);
        return new DataGridView(true);
    }


    /**
     * layui编辑器地址
     * @param file
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequiresPermissions(value = {"图片上传"})
    @RequestMapping("uploadImage")
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
     * 添加帖子
     * @param article
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @RequiresPermissions(value = {"修改帖子"})
    public ModelAndView update(Article article )throws Exception{
        Article oldArticle = articleService.get(article.getId());

        oldArticle.setName(article.getName());
        oldArticle.setArcType(article.getArcType());
        oldArticle.setContent(article.getContent());
        oldArticle.setDownload1(article.getDownload1());
        oldArticle.setPassword1(article.getPassword1());
        oldArticle.setPoints(article.getPoints());
        oldArticle.setPublishDate(new Date());
        if (oldArticle.getState() == 2){  //审核通过，要更新lucene索引
            articleIndex.updateIndex(oldArticle);
            if(redisUtil.hasKey("article_"+oldArticle.getId())){
                redisUtil.del("article_"+oldArticle.getId());
            }
        }
        articleService.save(oldArticle);

        ModelAndView andView = new ModelAndView();
        andView.addObject("title","修改帖子成功页面");
        andView.setViewName("admin/modifyArticleSuccess");
        return andView;
    }


    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("delete")
    @RequiresPermissions(value = {"删除帖子"})
    public DataGridView  delete(Integer id) throws Exception{
       userDownloadService.deleteByArticleId(id);
       commentService.deleteByArticleId(id);
        articleService.delete(id);
        articleIndex.delete(String.valueOf(id));
        //todo 删除redis缓存
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
    @RequiresPermissions(value = {"删除帖子"})
    public  DataGridView  deleteSelected(String ids) throws Exception{
        String[] idStr = ids.split(",");
        for (int i = 0; i < idStr.length; i++) {
            if(redisUtil.hasKey("article_"+idStr[i])){
                redisUtil.del("article_"+idStr[i]);
            }
            articleService.delete(Integer.parseInt(idStr[i]));
            userDownloadService.deleteByArticleId(Integer.parseInt(idStr[i]));
            commentService.deleteByArticleId(Integer.parseInt(idStr[i]));
            articleIndex.delete(String.valueOf(idStr[i]));
        }
        return new DataGridView(true);
    }

    /**
     * 修改帖子热门
     * @param article
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("updateHotState")
    @RequiresPermissions(value = {"修改热门状态"})
    public DataGridView updateHotState(Article article)throws Exception{
        Article oldArticle = articleService.get(article.getId());
        oldArticle.setHot(article.isHot());
        articleService.save(oldArticle);
        //todo 删除redis和lucene索引

        return new DataGridView(true);
    }



}
