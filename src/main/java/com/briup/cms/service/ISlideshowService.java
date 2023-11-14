package com.briup.cms.service;

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
}
