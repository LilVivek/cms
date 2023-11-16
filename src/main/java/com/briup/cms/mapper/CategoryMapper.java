package com.briup.cms.mapper;

import com.briup.cms.bean.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.briup.cms.bean.Extend.CategoryExtend;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Vivek
 * @since 2023-11-14
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    List<CategoryExtend> queryAllParent();
}
