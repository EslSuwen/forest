package com.rymcu.forest.web.api.v1.bank;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rymcu.forest.dto.BankAccountDTO;
import com.rymcu.forest.dto.BankAccountSearchDTO;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.service.BankAccountService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/** @author ronger */
@RestController
@RequestMapping("/api/admin/bank-account")
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
  public Result<BankAccountDTO> detail(@PathVariable Integer idUser) {
    return Result.OK(bankAccountService.findBankAccountByIdUser(idUser));
  }
}
