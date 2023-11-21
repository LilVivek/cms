package com.briup.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.briup.cms.bean.Extend.UserExtend;
import com.briup.cms.bean.User;
import com.briup.cms.exception.ServiceException;
import com.briup.cms.mapper.UserMapper;
import com.briup.cms.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.briup.cms.util.JwtUtil;
import com.briup.cms.util.MD5Utils;
import com.briup.cms.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Vivek
 * @since 2023-11-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public String login(String username, String password) {
        String token = null;
        // 根据用户名密码查询用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username)
                .eq(User::getPassword, password);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {//用户不存在
            throw new ServiceException(ResultCode.USER_LOGIN_ERROR);
        } else {//该用户名能在数据库中查到
            if (user.getPassword().equals(password)) {//前端传来的密码和数据库的密码做比对
                Map<String, Object> userMap = new HashMap<>();//这个userMap放在payLoad的Claim里
                userMap.put("userId", user.getId());
                userMap.put("username", user.getUsername());
                userMap.put("roleId", user.getRoleId());
                // 返回token字符串
                token = JwtUtil.sign(user.getId(), userMap);//第一个参数放在载荷payload的受众withAudience里，第二个放在载荷的声明withClaim里
            }
        }
        return token;
    }

    @Override
    public User queryById(Long id) {
        if (id == null) {
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new ServiceException(ResultCode.DATA_NONE);
        }
        return user;
    }

    @Override
    public void insert(User user) {
        if (user == null) {
            throw new ServiceException(ResultCode.DATA_NONE);
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        User user1 = userMapper.selectOne(wrapper);
        if (user1 != null) {//如果不为空说明用户名已存在
            throw new ServiceException(ResultCode.USERNAME_HAS_EXISTED);
        }
        if (!StringUtils.hasText(user.getUsername()) || !StringUtils.hasText(user.getPassword())) {
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }
        user.setPassword(MD5Utils.MD5(user.getPassword()));//密码用MD5加密
        user.setRegisterTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    @Override
    public void setVip(Long id) {
        if (id == null) {
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }
        if (user.getIsVip() == 1) {//已经设置为会员，不可重复设置会员状态和会员过期时间
            throw new ServiceException(ResultCode.DATA_EXISTED);
        }
        user.setIsVip(1);
        user.setExpiresTime(LocalDateTime.now().plusMonths(1));//加一个月
        userMapper.updateById(user);
    }

    @Override
    public void updateUser(User user) {
        if (user == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }
        Long id = user.getId();
        if (id == null || userMapper.selectById(id) == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }
        if (!StringUtils.hasText(user.getUsername())){//用户名不能为空
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }
        String newUsername = user.getUsername();//这里的newUsername可能发生了修改
        if (!newUsername.equals(userMapper.selectById(id).getUsername())) {//根据id查询原用户名是否产生了修改
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getUsername, newUsername);
            User selectOne = userMapper.selectOne(wrapper);
            if (selectOne != null) {//能在数据库里查询到说明不唯一
                throw new ServiceException(ResultCode.USERNAME_HAS_EXISTED);
            }
        }
        userMapper.updateById(user);
    }

    @Override
    public void deleteByBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException(ResultCode.PARAM_IS_INVALID);
        }
        int count = 0;
        for (Long id : ids) {
            User user = userMapper.selectById(id);
            if (user != null) {
                count++;
            }
        }
        if (count > 0) {//至少有一个id有效且存在
            userMapper.deleteBatchIds(ids);
        }else {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }
    }

    @Override
    public IPage<UserExtend> queryByPage(Integer pageNum, Integer pageSize, String username, String status, Integer roleId, Integer isVip) {
        if (pageNum==null||pageSize==null){
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }
        Page<UserExtend> page = new Page<>(pageNum, pageSize);
        IPage<UserExtend> pageWithRole=userMapper.selectPageWithRole(page,username, status, roleId, isVip);
        return pageWithRole;
    }
}
