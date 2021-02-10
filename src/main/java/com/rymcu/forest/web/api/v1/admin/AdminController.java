package com.rymcu.forest.web.api.v1.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.dto.admin.TopicTagDTO;
import com.rymcu.forest.dto.admin.UserRoleDTO;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.*;
import com.rymcu.forest.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/** @author ronger */
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

  @Resource private UserService userService;
  @Resource private RoleService roleService;
  @Resource private TopicService topicService;
  @Resource private TagService tagService;
  @Resource private SpecialDayService specialDayService;

  @GetMapping("/users")
  public Result<IPage<User>> users(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows) {
    // 按最后登录时间进行倒序排序
    IPage<User> list =
        userService.page(
            new Page<>(page, rows),
            new LambdaQueryWrapper<User>().orderByDesc(User::getLastLoginTime));
    return Result.OK(list);
  }

  @GetMapping("/user/{idUser}/role")
  public GlobalResult<List<Role>> userRole(@PathVariable Integer idUser) {
    List<Role> roles = roleService.findByIdUser(idUser);
    return GlobalResultGenerator.genSuccessResult(roles);
  }

  @GetMapping("/roles")
  public Result<IPage<Role>> roles(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows) {
    IPage<Role> list = roleService.page(new Page<>(page, rows));
    return Result.OK(list);
  }

  @PatchMapping("/user/update-role")
  public GlobalResult<Map> updateUserRole(@RequestBody UserRoleDTO userRole) {
    Map map = userService.updateUserRole(userRole.getIdUser(), userRole.getIdRole());
    return GlobalResultGenerator.genSuccessResult(map);
  }

  @PatchMapping("/user/update-status")
  public GlobalResult<Map> updateUserStatus(@RequestBody User user) {
    Map map = userService.updateStatus(user.getIdUser(), user.getStatus());
    return GlobalResultGenerator.genSuccessResult(map);
  }

  @PatchMapping("/role/update-status")
  public GlobalResult<Map> updateRoleStatus(@RequestBody Role role) {
    Map map = roleService.updateStatus(role.getIdRole(), role.getStatus());
    return GlobalResultGenerator.genSuccessResult(map);
  }

  @PostMapping("/role/post")
  public GlobalResult<Map> addRole(@RequestBody Role role) {
    Map map = roleService.saveRole(role);
    return GlobalResultGenerator.genSuccessResult(map);
  }

  @PutMapping("/role/post")
  public GlobalResult<Map> updateRole(@RequestBody Role role) {
    Map map = roleService.saveRole(role);
    return GlobalResultGenerator.genSuccessResult(map);
  }

  @GetMapping("/topics")
  public Result<IPage<Role>> topics(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows) {
    IPage<Role> list = roleService.page(new Page<>(page, rows));
    return Result.OK(list);
  }

  @GetMapping("/topic/{topicUri}")
  public GlobalResult topic(@PathVariable String topicUri) {
    if (StringUtils.isBlank(topicUri)) {
      return GlobalResultGenerator.genErrorResult("数据异常!");
    }
    Topic topic = topicService.findTopicByTopicUri(topicUri);
    return GlobalResultGenerator.genSuccessResult(topic);
  }

  @GetMapping("/topic/{topicUri}/tags")
  public GlobalResult topicTags(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows,
      @PathVariable String topicUri) {
    if (StringUtils.isBlank(topicUri)) {
      return GlobalResultGenerator.genErrorResult("数据异常!");
    }
    Map map = topicService.findTagsByTopicUri(topicUri, page, rows);
    return GlobalResultGenerator.genSuccessResult(map);
  }

  @GetMapping("/topic/detail/{idTopic}")
  public GlobalResult<Topic> topicDetail(@PathVariable Integer idTopic) {
    Topic topic = topicService.getById(idTopic.toString());
    return GlobalResultGenerator.genSuccessResult(topic);
  }

  @GetMapping("/topic/unbind-topic-tags")
  public Result<?> unbindTopicTags(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows,
      @RequestParam Integer idTopic,
      @RequestParam String tagTitle) {
    // TODO 等待重构
    List<Tag> list = topicService.findUnbindTagsById(idTopic, tagTitle);
    return Result.error("等待重构");
  }

  @PostMapping("/topic/bind-topic-tag")
  public GlobalResult bindTopicTag(@RequestBody TopicTagDTO topicTag) {
    Map map = topicService.bindTopicTag(topicTag);
    return GlobalResultGenerator.genSuccessResult(map);
  }

  @DeleteMapping("/topic/unbind-topic-tag")
  public GlobalResult unbindTopicTag(@RequestBody TopicTagDTO topicTag) {
    Map map = topicService.unbindTopicTag(topicTag);
    return GlobalResultGenerator.genSuccessResult(map);
  }

  @PostMapping("/topic/post")
  public GlobalResult<Map> addTopic(@RequestBody Topic topic) {
    Map map = topicService.saveTopic(topic);
    return GlobalResultGenerator.genSuccessResult(map);
  }

  @PutMapping("/topic/post")
  public GlobalResult<Map> updateTopic(@RequestBody Topic topic) {
    Map map = topicService.saveTopic(topic);
    return GlobalResultGenerator.genSuccessResult(map);
  }

  @GetMapping("/tags")
  public Result<IPage<Tag>> tags(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows) {
    IPage<Tag> list = tagService.page(new Page<>(page, rows));
    return Result.OK(list);
  }

  @DeleteMapping("/tag/clean-unused")
  public Result<?> cleanUnusedTag() {
    return Result.OK(tagService.cleanUnusedTag());
  }

  @GetMapping("/tag/detail/{idTag}")
  public Result<Tag> tagDetail(@PathVariable Integer idTag) {
    return Result.OK(tagService.getById(idTag));
  }

  @RequestMapping(
      value = "/tag/post",
      method = {RequestMethod.POST, RequestMethod.PUT})
  public Result<?> addTag(@RequestBody Tag tag) {
    return Result.OK(tagService.saveTag(tag));
  }

  @GetMapping("/special-days")
  public Result<IPage<SpecialDay>> specialDays(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows) {
    IPage<SpecialDay> list = specialDayService.page(new Page<>(page, rows));
    return Result.OK(list);
  }
}
