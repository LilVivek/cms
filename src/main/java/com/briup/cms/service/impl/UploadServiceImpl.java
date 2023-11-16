package com.briup.cms.service.impl;

import com.briup.cms.exception.ServiceException;
import com.briup.cms.service.IUploadService;
import com.briup.cms.service.IUserService;
import com.briup.cms.util.ResultCode;
import com.briup.cms.util.UploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class UploadServiceImpl implements IUploadService {
    @Autowired
    UploadUtils uploadUtils;

    @Override
    public String upLoad(MultipartFile img) {
        String url = null;/*这里的url用于上传成功后回显的url*/
        try {
            //上传到本地nginx服务器
            // url = uploadUtils.fileToLocal(file);//没写这方法
            //上传到七牛云服务器
            url = uploadUtils.fileToOSS(img);
        } catch (Exception e) {
            log.info("文件上传失败,原因:{}", e.getMessage());
            throw new
                    ServiceException(ResultCode.SYSTEM_INNER_ERROR);
        }
        return url;

    }
}
