package com.rymcu.forest.web.api.v1.bank;

import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.TransactionRecord;
import com.rymcu.forest.service.TransactionRecordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/** @author ronger */
@RestController
@RequestMapping("/api/transaction")
public class TransactionRecordController {

  @Resource private TransactionRecordService transactionRecordService;

  @PostMapping("/transfer")
  public Result<TransactionRecord> transfer(@RequestBody TransactionRecord transactionRecord)
      throws Exception {
    return Result.OK(transactionRecordService.transfer(transactionRecord));
  }
}
