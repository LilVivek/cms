package com.briup.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.briup.cms.bean.Extend.LogExportParam;
import com.briup.cms.bean.Extend.LogParam;
import com.briup.cms.bean.Log;
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
public interface ILogService extends IService<Log> {

    IPage<Log> select(LogParam param);

    List<Log> queryForExport(LogExportParam logExportParam);
}
