package com.briup.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.briup.cms.bean.Slideshow;
import com.briup.cms.exception.ServiceException;
import com.briup.cms.mapper.SlideshowMapper;
import com.briup.cms.service.ISlideshowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.briup.cms.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Vivek
 * @since 2023-11-14
 */
@Service
public class SlideshowServiceImpl extends ServiceImpl<SlideshowMapper, Slideshow> implements ISlideshowService {
    @Autowired
    SlideshowMapper slideshowMapper;

    @Override
    public List<Slideshow> queryAllEnable() {
        LambdaQueryWrapper<Slideshow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Slideshow::getStatus, "启用");
        wrapper.orderByDesc(Slideshow::getUploadTime);
        List<Slideshow> list = slideshowMapper.selectList(wrapper);
        if (list == null || list.size() == 0)//没有数据
            throw new ServiceException(ResultCode.DATA_NONE);
        return list;

    }
}
