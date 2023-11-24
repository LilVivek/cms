package com.briup.cms.util.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.briup.cms.bean.Category;
import com.briup.cms.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class CategoryListener implements ReadListener<Category> {
    //    @Autowired// SpringIOC容器的初始化在监听器和转换器创建之后 所以注入为null
    private final ICategoryService iCategoryService;//因为无法使用自动注入，所以用构造器传入

    public CategoryListener(ICategoryService iCategoryService) {
        this.iCategoryService = iCategoryService;
    }

    List<Category> list = new ArrayList<>(100);//(存二级栏目的集合)初始设置大容量，防止自动扩容浪费性能


    @Override
    public void invoke(Category category, AnalysisContext analysisContext) {
        if (category.getParentId() == null) {//如果是一级栏目 不需要等 直接插入数据库，不然后续二级栏目如果是这个一级栏目的子栏目，就会在转换器抛异常
            iCategoryService.insert(category);//必须马上插入一级栏目 不然下一个如果是该一次栏目下属的二级栏目 在转换器就会抛异常
            ParentIdConverter.list = iCategoryService.queryAllOneLevel();//每次插一个一次栏目就要更新转换器里装一级栏目的list
        }
        list.add(category);
        if (list.size() >= 100) {
            iCategoryService.insertInBatch(list);
//            iCategoryService.saveBatch(list);//这里也可以用mp自带的批量插入方法
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        iCategoryService.insertInBatch(list);
        list.clear();
        System.out.println("全部解析完毕");
    }
}
