package com.rymcu.forest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/** @author ronger */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("forest_sponsor")
public class Sponsor implements Serializable, Cloneable {
  /** 主键 */
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
  /** 数据类型 */
  private String dataType;
  /** 数据主键 */
  private Integer dataId;
  /** 赞赏人 */
  private Integer sponsor;
  /** 赞赏日期 */
  private Date sponsorshipTime;
  /** 赞赏金额 */
  private BigDecimal sponsorshipMoney;
}
