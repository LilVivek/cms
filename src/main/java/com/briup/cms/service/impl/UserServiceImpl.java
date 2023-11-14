package com.briup.cms.service.impl;

import com.briup.cms.bean.User;
import com.briup.cms.mapper.UserMapper;
import com.briup.cms.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Vivek
 * @since 2023-11-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
