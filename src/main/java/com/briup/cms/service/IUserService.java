package com.briup.cms.service;

import com.briup.cms.bean.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Vivek
 * @since 2023-11-14
 */
public interface IUserService extends IService<User> {

    String login(String username, String password);

    User queryById(Long id);
}
