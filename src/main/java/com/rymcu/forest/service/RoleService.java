package com.rymcu.forest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.Role;
import com.rymcu.forest.entity.User;

import java.util.List;

/**
 * @author CodeGenerator
 * @date 2018/05/29
 */
public interface RoleService extends IService<Role> {

  /**
   * 查询用户角色
   *
   * @param user
   * @return
   */
  List<Role> selectRoleByUser(User user);

  /**
   * 查询用户角色
   *
   * @param idUser
   * @return
   */
  List<Role> findByIdUser(Integer idUser);

  /**
   * 更新用户状态
   *
   * @param idRole
   * @param status
   * @return
   */
  Result<?> updateStatus(Integer idRole, String status);

  /**
   * 添加/更新角色
   *
   * @param role
   * @return
   */
  Role saveRole(Role role);
}
