package com.rymcu.forest.web.api.v1.bank;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.dto.BankAccountDTO;
import com.rymcu.forest.dto.BankAccountSearchDTO;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.service.BankAccountService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/** @author ronger */
@RestController
@RequestMapping("/api/v1/admin/bank-account")
public class BankAccountController {

  @Resource private BankAccountService bankAccountService;

  @GetMapping("/list")
  public Result<List<BankAccountDTO>> banks(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows,
      BankAccountSearchDTO bankAccountSearchDTO) {
    List<BankAccountDTO> list =
        bankAccountService.findBankAccounts(new Page<>(page, rows), bankAccountSearchDTO);
    return Result.OK(list);
  }

  @GetMapping("/{idUser}")
  public GlobalResult detail(@PathVariable Integer idUser) {
    BankAccountDTO bankAccount = bankAccountService.findBankAccountByIdUser(idUser);
    return GlobalResultGenerator.genSuccessResult(bankAccount);
  }
}
