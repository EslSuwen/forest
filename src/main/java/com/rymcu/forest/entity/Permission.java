package com.rymcu.forest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/** @author ronger */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("forest_permission")
public class Permission implements Serializable, Cloneable {
  @TableId(value = "id", type = IdType.AUTO)
  private Integer idPermission;
  /** 权限标识 */
  private String permissionCategory;
}
