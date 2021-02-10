package com.rymcu.forest.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/** @author ronger */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("forest_role")
public class Role implements Serializable, Cloneable {
  @TableId(value = "id", type = IdType.AUTO)
  private Integer idRole;
  /** 角色名称 */
  private String name;
  /** 拼音码 */
  private String inputCode;
  /** 权重 */
  private Integer weights;
  /** 状态 */
  private String status;
  /** 创建时间 */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date createdTime;
  /** 更新时间 */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date updatedTime;
}
