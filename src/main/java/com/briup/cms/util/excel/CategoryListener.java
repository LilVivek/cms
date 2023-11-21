package com.briup.cms.util.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
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

    List<Category> list = new ArrayList<>(100);//初始设置大容量，防止自动扩容浪费性能


    @Override
    public void invoke(Category category, AnalysisContext analysisContext) {
        list.add(category);
        if (list.size() >= 100) {
            iCategoryService.insertInBatch(list);
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
