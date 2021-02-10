package com.rymcu.forest.web.api.v1.bank;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rymcu.forest.dto.BankDTO;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.service.BankService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/** @author ronger */
@RestController
@RequestMapping("/api/v1/admin/bank")
public class BankController {

  @Resource private BankService bankService;

  @GetMapping("/list")
  public Result<List<BankDTO>> banks(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows) {
    List<BankDTO> list = bankService.findBanks(new Page<>(page, rows));
    return Result.OK(list);
  }
}
