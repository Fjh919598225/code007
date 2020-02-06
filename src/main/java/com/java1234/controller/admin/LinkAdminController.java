package com.java1234.controller.admin;

import com.java1234.common.DataGridView;
import com.java1234.entity.Link;
import com.java1234.init.InitSystem;
import com.java1234.service.LinkService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员友情链接控制器
 * @Date 2020/2/1 22:19
 * @Author JianHui
 */
@Controller
@RequestMapping("admin/link")
public class LinkAdminController {

    @Autowired
    private LinkService linkService;

    @Resource
    private InitSystem initSystem;

    @ResponseBody
    @RequestMapping("list")
    @RequiresPermissions(value = {"分页查询友情链接"})
    public DataGridView list(@RequestParam(value = "page",required = false)Integer page
            ,@RequestParam(value = "limit",required = false)Integer limit){
        List<Link> linkList = linkService.list(page, limit, Sort.Direction.ASC, "sort");
        Long total = linkService.getTotal();
        return new DataGridView(0,total,linkList);
    }

    @ResponseBody
    @RequestMapping("save")
    @RequiresPermissions(value = {"添加或者修改友情链接"})
    public DataGridView save(Link link , HttpServletRequest request){
        linkService.save(link);
        initSystem.loadData(request.getServletContext());
        return new DataGridView(true);
    }

    @ResponseBody
    @RequestMapping("delete")
    @RequiresPermissions(value = {"删除友情链接"})
    public DataGridView delete(Integer id,HttpServletRequest request){
        linkService.delete(id);
        initSystem.loadData(request.getServletContext());
        return new DataGridView(true);
    }

    @ResponseBody
    @RequestMapping("findById")
    @RequiresPermissions(value = {"根据id查询友情链接实体"})
    public Map<String,Object> findById(Integer id){
        Link link = linkService.get(id);
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        map.put("link",link);
        return map;
    }

}
