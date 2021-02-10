package com.rymcu.forest.lucene.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
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
    IPage<UserDic> list = dicService.page(new Page<>(pageNum, pageSize));
    return Result.OK(list);
  }

  @PostMapping("/addDic/{dic}")
  public GlobalResult addDic(@PathVariable String dic) {
    dicService.addDic(dic);
    return GlobalResultGenerator.genSuccessResult("新增字典成功");
  }

  @PutMapping("/editDic")
  public GlobalResult getAllDic(@RequestBody UserDic dic) {
    dicService.updateDic(dic);
    return GlobalResultGenerator.genSuccessResult("更新字典成功");
  }

  @DeleteMapping("/deleteDic/{id}")
  public GlobalResult deleteDic(@PathVariable String id) {
    dicService.deleteDic(id);
    return GlobalResultGenerator.genSuccessResult("删除字典成功");
  }
}
