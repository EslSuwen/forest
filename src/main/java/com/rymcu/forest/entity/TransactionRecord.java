package com.rymcu.forest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易记录
 *
 * @author ronger
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("forest_transaction_record")
public class TransactionRecord {
  /** 主键 */
  @TableId(value = "id", type = IdType.AUTO)
  private Integer idTransactionRecord;
  /** 交易流水号 */
  private String transactionNo;
  /** 款项 */
  private String funds;
  /** 交易发起方 */
  private String formBankAccount;
  /** 交易收款方 */
  private String toBankAccount;
  /** 交易金额 */
  private BigDecimal money;
  /** 交易类型 */
  private String transactionType;
  /** 交易时间 */
  private Date transactionTime;
}
