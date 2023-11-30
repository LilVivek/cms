package com.briup.cms.web.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.briup.cms.bean.Category;
import com.briup.cms.bean.Extend.LogExportParam;
import com.briup.cms.bean.Extend.LogParam;
import com.briup.cms.bean.Log;
import com.briup.cms.service.ILogService;
import com.briup.cms.util.AOP.MarkupJoinPoint;
import com.briup.cms.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Vivek
 * @since 2023-11-14
 */
@Api(tags = "日志模块")
@RestController
@RequestMapping("/auth/log")
public class LogController {
    @Autowired
    private ILogService iLogService;

    @ApiOperation(value = "分页+条件查询日志信息", notes = "用户名、时间范围可以为空")
    @PostMapping("/query")
    public Result query(@RequestBody LogParam param) {
        IPage<Log> page = iLogService.select(param);

        return Result.success(page);
    }
    //文件以响应 流的方式输出,不需要返回值
    @MarkupJoinPoint
    @ApiOperation("导出日志信息")
    @GetMapping(value = "/export", produces = "application/octet-stream")
    public void export(HttpServletResponse response,LogExportParam logExportParam) throws IOException {
        //1.获取数据
        List<Log> list = iLogService.queryForExport(logExportParam);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);//这两个是必须的
        response.setCharacterEncoding("utf-8");//这两个是必须的
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + "日志信息表" + ".xlsx");
        //2.导出数据
        EasyExcel.write(response.getOutputStream(), Log.class).sheet("日志信息表").doWrite(list);
    }
}
