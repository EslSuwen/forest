package com.rymcu.forest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rymcu.forest.entity.Role;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.mapper.RoleMapper;
import com.rymcu.forest.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CodeGenerator
 * @date 2018/05/29
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
  @Resource private RoleMapper roleMapper;

  @Override
  public List<Role> selectRoleByUser(User sysUser) {
    List<Role> roles = roleMapper.selectRoleByIdUser(sysUser.getIdUser());
    return roles;
  }

  @Override
  public List<Role> findByIdUser(Integer idUser) {
    return roleMapper.selectRoleByIdUser(idUser);
  }

  @Override
  @Transactional
  public Map updateStatus(Integer idRole, String status) {
    Map map = new HashMap(1);
    Integer result = roleMapper.updateStatus(idRole, status);
    if (result == 0) {
      map.put("message", "更新失败!");
    }
    return map;
  }

  @Override
  public Map saveRole(Role role) {
    if (role.getIdRole() == null) {
      role.setCreatedTime(new Date());
      role.setUpdatedTime(role.getCreatedTime());
      save(role);
    } else {
      role.setCreatedTime(new Date());
      roleMapper.update(role.getIdRole(), role.getName(), role.getInputCode(), role.getWeights());
    }
    Map map = new HashMap(1);
    map.put("role", role);
    return map;
  }
}
