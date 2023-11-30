package com.briup.cms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.briup.cms.bean.Extend.LogExportParam;
import com.briup.cms.bean.Extend.LogParam;
import com.briup.cms.bean.Log;
import com.briup.cms.exception.ServiceException;
import com.briup.cms.mapper.LogMapper;
import com.briup.cms.service.ILogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.briup.cms.util.ResultCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletResponse;
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
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {
    @Autowired
    LogMapper logMapper;

    @Override
    public IPage<Log> select(LogParam param) {
        if (param == null || param.getPageNum() == null || param.getPageSize() == null) {
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }
        Page<Log> page = new Page<>(param.getPageNum(), param.getPageSize());
        LambdaQueryChainWrapper<Log> wrapper = new LambdaQueryChainWrapper<>(logMapper);
        wrapper.like(StringUtils.hasText(param.getUsername()), Log::getUsername, param.getUsername())
                .like(StringUtils.hasText(param.getRequestUrl()), Log::getRequestUrl, param.getRequestUrl())
                .le(param.getEndTime() != null, Log::getCreateTime, param.getEndTime())
                .ge(param.getStartTime() != null, Log::getCreateTime, param.getStartTime())
                .orderByDesc(Log::getCreateTime)
                .page(page);
        return page;
    }

    @Override
    public List<Log> queryForExport(LogExportParam logExportParam) {
        List<Log> list = logMapper.selectList(null);
        return list;
    }


}
