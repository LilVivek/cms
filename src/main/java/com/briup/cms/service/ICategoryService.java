package com.briup.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.briup.cms.bean.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.briup.cms.bean.Extend.CategoryExtend;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Vivek
 * @since 2023-11-14
 */
public interface ICategoryService extends IService<Category> {

    void insert(Category category);

    Category getCategoryById(Integer id);

    void update(Category category);

    void deleteById(Integer id);

    void deleteByIdAll(List<Integer> ids);

    IPage<Category> query(Integer pageNum, Integer pageSize, Integer parentId);

    List<CategoryExtend> queryAllParent();

    List<Category> queryAllOneLevel();

    void insertInBatch(List<Category> list);

    List<Category> queryAll();
}
