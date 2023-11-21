package com.briup.cms.web.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.briup.cms.bean.Category;
import com.briup.cms.bean.Extend.CategoryExtend;
import com.briup.cms.service.IArticleService;
import com.briup.cms.service.ICategoryService;
import com.briup.cms.util.Result;
import com.briup.cms.util.excel.CategoryListener;
import com.briup.cms.util.excel.ParentIdConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Vivek
 * @since 2023-11-14
 */
@Api(tags = "栏目模块")
@Slf4j
@RestController
@RequestMapping("/auth/category")
public class CategoryController {
    @Autowired
    ICategoryService iCategoryService;

    @ApiOperation("新增栏目")
    @PostMapping("/save")
    public Result save(@RequestBody Category category) {
        iCategoryService.insert(category);
        return Result.success("新增成功");
    }

    @ApiOperation("根据id查询栏目信息")
    @GetMapping("/getCategoryById/{id}")
    public Result getCategoryById(@PathVariable("id") Integer id) {
        Category category = iCategoryService.getCategoryById(id);
        return Result.success(category);
    }

    @ApiOperation("更新栏目")
    @PutMapping("/update")
    public Result update(@RequestBody Category category) {
        iCategoryService.update(category);
        return Result.success("修改成功");
    }

    @ApiOperation("根据id删除栏目")
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        iCategoryService.deleteById(id);
        return Result.success("删除成功");
    }

    @ApiOperation("批量删除栏目")
    @DeleteMapping("/deleteByIdAll/{ids}")
    public Result deleteByIdAll(@PathVariable("ids") List<Integer> ids) {
        iCategoryService.deleteByIdAll(ids);
        return Result.success("删除成功");
    }

    @ApiOperation(value = "分页查询所有栏目")
    @GetMapping("/query")
    public Result query(Integer pageNum, Integer pageSize, Integer parentId) {
        IPage<Category> p = iCategoryService.query(pageNum, pageSize, parentId);
        return Result.success(p);
    }

    @ApiOperation(value = " 查询所有1级栏目(含2级)")
    @GetMapping("/queryAllParent")
    public Result queryAllParent() {
        List<CategoryExtend> list = iCategoryService.queryAllParent();
        return Result.success(list);
    }

    @ApiOperation(value = " 获取所有父栏目")
    @GetMapping("/queryAllOneLevel")
    public Result queryAllOneLevel() {
        List<Category> list = iCategoryService.queryAllOneLevel();
        return Result.success(list);
    }

    @ApiOperation(value = "导入栏目数据")
    @PostMapping("/import")
    public Result imports(@RequestPart MultipartFile file) throws IOException {
        ParentIdConverter.list = iCategoryService.queryAllOneLevel();
        //尽量不要用doReadSync()方法来获取返回值，当数据量过大时全存在内存可能导致OOM(在监听器里处理list)
        EasyExcel.read(file.getInputStream(), Category.class, new CategoryListener(iCategoryService)).sheet().doRead();
        return Result.success("数据导入成功");
    }

    @ApiOperation("导出栏目数据")
    @GetMapping(value = "/export", produces = "application/octet-stream")
    public void exports(HttpServletResponse response) throws IOException {
        ParentIdConverter.list = iCategoryService.queryAllOneLevel();
        //1.获取栏目数据
        List<Category> list = iCategoryService.queryAll();
        //2.导出数据(导出不需要监听器，导入需要监听器)
//        EasyExcel.write(new File("C:\\Users\\Vivek\\Desktop\\test.xlsx"), Category.class).sheet().doWrite(list);//测试用文件
        EasyExcel.write(response.getOutputStream(), Category.class).sheet().doWrite(list);
    }


}
