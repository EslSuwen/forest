package com.rymcu.forest.web.api.v1.follow;

import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.Follow;
import com.rymcu.forest.service.FollowService;
import com.rymcu.forest.web.api.v1.exception.BaseApiException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/** @author ronger */
@RestController
@RequestMapping("/api/follow")
public class FollowController {

  @Resource private FollowService followService;

  @GetMapping("/is-follow")
  public Result<?> isFollow(@RequestParam Integer followingId, @RequestParam String followingType)
      throws BaseApiException {
    return Result.OK(followService.isFollow(followingId, followingType));
  }

  @PostMapping
  public Result<?> follow(@RequestBody Follow follow) throws BaseApiException {
    return Result.OK(followService.follow(follow));
  }

  @PostMapping("cancel-follow")
  public Result<?> cancelFollow(@RequestBody Follow follow) throws BaseApiException {
    return Result.OK(followService.cancelFollow(follow));
  }
}
