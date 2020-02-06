package com.java1234.controller.admin;

import com.java1234.common.DataGridView;
import com.java1234.entity.ArcType;
import com.java1234.init.InitSystem;
import com.java1234.service.ArcTypeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员资源类别控制器
 * @Date 2020/2/1 22:19
 * @Author JianHui
 */
@Controller
@RequestMapping("admin/arcType")
public class ArcTypeAdminController {

    @Autowired
    private ArcTypeService arcTypeService;

    @Resource
    private InitSystem initSystem;

    @ResponseBody
    @RequestMapping("list")
    @RequiresPermissions(value = {"分页查询资源类别信息"})
    public DataGridView list(@RequestParam(value = "page",required = false)Integer page
            ,@RequestParam(value = "limit",required = false)Integer limit){
        List<ArcType> arcTypeList = arcTypeService.list(page, limit, Sort.Direction.ASC, "sort");
        Long total = arcTypeService.getTotal();
        return new DataGridView(0,total,arcTypeList);
    }

    @ResponseBody
    @RequestMapping("save")
    @RequiresPermissions(value = {"添加或者修改类别信息"})
    public DataGridView save(ArcType arcType , HttpServletRequest request){
        arcTypeService.save(arcType);
        initSystem.loadData(request.getServletContext());
        return new DataGridView(true);
    }

    @ResponseBody
    @RequestMapping("delete")
    @RequiresPermissions(value = {"删除类别信息"})
    public DataGridView delete(Integer id,HttpServletRequest request){
        arcTypeService.delete(id);
        initSystem.loadData(request.getServletContext());
        return new DataGridView(true);
    }

    @ResponseBody
    @RequestMapping("findById")
    @RequiresPermissions(value = {"根据id查询资源类别实体"})
    public Map<String,Object> findById(Integer id){
        ArcType arcType = arcTypeService.get(id);
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        map.put("arcType",arcType);
        return map;
    }

}
