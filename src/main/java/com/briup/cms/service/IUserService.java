package com.briup.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.briup.cms.bean.Extend.UserExtend;
import com.briup.cms.bean.User;
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
public interface IUserService extends IService<User> {

    String login(String username, String password);

    User queryById(Long id);

    void insert(User user);

    void setVip(Long id);

    void updateUser(User user);

    void deleteByBatch(List<Long> ids);

    IPage<UserExtend> queryByPage(Integer pageNum, Integer pageSize, String username, String status, Integer roleId, Integer isVip);
}
