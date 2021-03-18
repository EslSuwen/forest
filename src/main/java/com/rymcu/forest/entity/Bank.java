package com.rymcu.forest.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 银行
 *
 * @author ronger
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("forest_bank")
public class Bank {

  /** 主键 */
  @TableId(value = "id", type = IdType.AUTO)
  private Integer idBank;
  /** 银行名称 */
  private String bankName;
  /** 银行负责人 */
  private Integer bankOwner;
  /** 银行描述 */
  private String bankDescription;
  /** 创建人 */
  private Integer createdBy;
  /** 创建时间 */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date createdTime;
}
