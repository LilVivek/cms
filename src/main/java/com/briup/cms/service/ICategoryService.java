package com.briup.cms.service;

import com.briup.cms.bean.Category;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
