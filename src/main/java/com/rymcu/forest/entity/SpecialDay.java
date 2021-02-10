package com.rymcu.forest.entity;

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
@TableName("forest_special_day")
public class SpecialDay implements Serializable, Cloneable {
  /** 主键 */
  @TableId(value = "id", type = IdType.AUTO)
  private Integer idSpecialDay;
  /** 名称 */
  private String specialDayName;
  /** 权重/优先级,小数优秀 */
  private Integer weights;
  /** 开始时间 */
  private Date startTime;
  /** 过期时间 */
  private Date expirationTime;
  /** 是否重复 */
  private Integer repeat;
  /** 重复周期 */
  private Integer repeatCycle;
  /** 0:天1:周2:月3:年 */
  private Integer repeatCycleUnit;
  /** 创建时间 */
  private Date createdTime;
  /** 图片路径 */
  private String imgUrl;
  /** 执行全局样式 */
  private String cssStyle;
}
