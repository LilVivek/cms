package com.briup.cms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.briup.cms.bean.Slideshow;
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
public interface ISlideshowService extends IService<Slideshow> {

    List<Slideshow> queryAllEnable();

    Page<Slideshow> query(Integer pageNum, Integer pageSize, String desc, String status);

    Slideshow queryById(Integer id);

    boolean saveOrUpdate(Slideshow slideshow);

    void deleteByBatch(List<Integer> ids);
}
