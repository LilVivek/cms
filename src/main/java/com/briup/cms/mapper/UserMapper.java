package com.briup.cms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.briup.cms.bean.Extend.UserExtend;
import com.briup.cms.bean.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Vivek
 * @since 2023-11-14
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    //分页+条件查询用户(含角色) /*只要返回类型是分页Page类型 传入page对象就可以自动实现分页*/
    IPage<UserExtend> selectPageWithRole(Page<UserExtend> page,
                                         @Param("username") String username,
                                         @Param("status") String status,
                                         @Param("roleId") Integer roleId,
                                         @Param("isVip") Integer isVip);
}
