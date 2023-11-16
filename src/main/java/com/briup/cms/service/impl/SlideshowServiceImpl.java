package com.briup.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.briup.cms.bean.Slideshow;
import com.briup.cms.exception.ServiceException;
import com.briup.cms.mapper.SlideshowMapper;
import com.briup.cms.service.ISlideshowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.briup.cms.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
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
        wrapper.eq(Slideshow::getStatus, "启用")
                .orderByDesc(Slideshow::getUploadTime);
        List<Slideshow> list = slideshowMapper.selectList(wrapper);//list最多是空集合，不可能返回null
        if (list.size() == 0)//没有数据
            throw new ServiceException(ResultCode.DATA_NONE);
        return list;

    }

    @Override
    public Page<Slideshow> query(Integer pageNum, Integer pageSize, String desc, String status) {
        Page<Slideshow> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Slideshow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(status), Slideshow::getStatus, status)
                .like(StringUtils.hasText(desc), Slideshow::getDescription, desc)
                .orderByDesc(Slideshow::getUploadTime);
        Page<Slideshow> list = slideshowMapper.selectPage(page, wrapper);
        if (list.getTotal() == 0) {
            throw new ServiceException(ResultCode.DATA_NONE);
        }

        return list;
    }

    @Override
    public Slideshow queryById(Integer id) {
//        LambdaQueryWrapper<Slideshow> wrapper = new LambdaQueryWrapper<>();
        Slideshow result = slideshowMapper.selectById(id);
        if (result == null) {
            throw new ServiceException(ResultCode.DATA_NONE);
        }
        return result;
    }

    @Override
    public void insertOrUpdate(Slideshow slideshow) {
        boolean flag = false;//标识url是否是原来的值
        if (null != slideshow.getUrl()) {
            if (null != slideshow.getId()) {//当更新操作时
                Slideshow oldSlideShow = slideshowMapper.selectById(slideshow.getId());
                if (oldSlideShow != null && oldSlideShow.getUrl().equals(slideshow.getUrl())) {//当传入url的值等于原来的值
                    flag = true;
                }
            }
            if (!flag) {//如果url不是原来的值(更改了图片)，开始写判断新url是否与旧url冲突的逻辑
                LambdaQueryWrapper<Slideshow> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Slideshow::getUrl, slideshow.getUrl());
                Slideshow selectOne = slideshowMapper.selectOne(wrapper);
                if (selectOne != null) {//查到和舊的url衝突
                    throw new ServiceException(ResultCode.SLIDESHOW_URL_EXISTED);
                }
                slideshow.setUploadTime(LocalDateTime.now());//更新图片上传时间
            }
        }
        if (slideshow.getId() == null) {//新增
            if (!StringUtils.hasText(slideshow.getStatus())) {//如果没传入状态信息,默认启用
                slideshow.setStatus("启用");
            }
            slideshowMapper.insert(slideshow);
        } else {//修改
            Slideshow slideshow1 = slideshowMapper.selectById(slideshow.getId());
            if (slideshow1 == null) {//如果根据id在数据库中查不到对应的数据
                throw new ServiceException(ResultCode.SLIDESHOW_NOT_EXISTED);
            }
            slideshowMapper.updateById(slideshow);
        }
    }

    @Override
    public void deleteByBatch(List<Integer> ids) {
        if (ids == null) {//參數爲空
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }
        LambdaQueryWrapper<Slideshow> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Slideshow::getId, ids);
        List<Slideshow> list = slideshowMapper.selectList(wrapper);
        if (list == null || list.size() == 0) {//如果一個id都沒查到對應的數據
            throw new ServiceException(ResultCode.SLIDESHOW_NOT_EXISTED);
        }
        slideshowMapper.deleteBatchIds(ids);
    }
}
