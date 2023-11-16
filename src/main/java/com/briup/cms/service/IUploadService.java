package com.briup.cms.service;

import org.springframework.web.multipart.MultipartFile;

public interface IUploadService {

    String upLoad(MultipartFile img);
}
