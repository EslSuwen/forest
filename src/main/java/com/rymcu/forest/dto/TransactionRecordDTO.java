package com.rymcu.forest.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/** @author ronger */
@Data
public class TransactionRecordDTO {

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
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date transactionTime;
}
