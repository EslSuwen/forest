package com.rymcu.forest.lucene.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.lucene.model.UserDic;
import com.rymcu.forest.lucene.service.UserDicService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * UserDicController
 *
 * @author suwen
 * @date 2021/2/4 09:29
 */
@RestController
@RequestMapping("/api/v1/lucene/dic")
public class UserDicController {

  @Resource private UserDicService dicService;

  @GetMapping("/getAll")
  public Result<IPage<UserDic>> getAll(
      @RequestParam(defaultValue = "0") Integer pageNum,
      @RequestParam(defaultValue = "10") Integer pageSize) {
    return Result.OK(dicService.page(new Page<>(pageNum, pageSize)));
  }

  @PostMapping("/addDic/{dic}")
  public Result<?> addDic(@PathVariable String dic) {
    dicService.addDic(dic);
    return Result.OK();
  }

  @PutMapping("/editDic")
  public Result<?> getAllDic(@RequestBody UserDic dic) {
    dicService.updateDic(dic);
    return Result.OK();
  }

  @DeleteMapping("/deleteDic/{id}")
  public Result<?> deleteDic(@PathVariable String id) {
    dicService.deleteDic(id);
    return Result.OK();
  }
}
