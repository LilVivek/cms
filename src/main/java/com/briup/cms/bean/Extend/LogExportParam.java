package com.briup.cms.bean.Extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Data
public class LogExportParam {

    //发送请求的用户
    private String username;
    //请求的url
    private String requestUrl;

    //待导出数据的条数
    private Integer count;

    //日志时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}