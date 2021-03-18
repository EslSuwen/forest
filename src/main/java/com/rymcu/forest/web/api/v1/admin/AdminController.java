package com.rymcu.forest.web.api.v1.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rymcu.forest.dto.admin.TopicTagDTO;
import com.rymcu.forest.dto.admin.UserRoleDTO;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.*;
import com.rymcu.forest.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/** @author ronger */
@RestController
@RequestMapping("/api/admin")
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
  public Result<List<Role>> userRole(@PathVariable Integer idUser) {
    return Result.OK(roleService.findByIdUser(idUser));
  }

  @GetMapping("/roles")
  public Result<IPage<Role>> roles(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows) {
    return Result.OK(roleService.page(new Page<>(page, rows)));
  }

  @PatchMapping("/user/update-role")
  public Result<?> updateUserRole(@RequestBody UserRoleDTO userRole) {
    return userService.updateUserRole(userRole.getIdUser(), userRole.getIdRole());
  }

  @PatchMapping("/user/update-status")
  public Result<?> updateUserStatus(@RequestBody User user) {
    return userService.updateStatus(user.getIdUser(), user.getStatus());
  }

  @PatchMapping("/role/update-status")
  public Result<?> updateRoleStatus(@RequestBody Role role) {
    return roleService.updateStatus(role.getIdRole(), role.getStatus());
  }

  @RequestMapping(
      value = "/role/post",
      method = {RequestMethod.POST, RequestMethod.PUT})
  public Result<Role> addRole(@RequestBody Role role) {
    return Result.OK(roleService.saveRole(role));
  }

  @GetMapping("/topics")
  public Result<IPage<Topic>> topics(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows) {
    IPage<Topic> list = topicService.page(new Page<>(page, rows));
    return Result.OK(list);
  }

  @GetMapping("/topic/{topicUri}")
  public Result<?> topic(@PathVariable String topicUri) {
    if (StringUtils.isBlank(topicUri)) {
      return Result.error("数据异常!");
    }
    return Result.OK(topicService.findTopicByTopicUri(topicUri));
  }

  @GetMapping("/topic/{topicUri}/tags")
  public Result<?> topicTags(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows,
      @PathVariable String topicUri) {
    if (StringUtils.isBlank(topicUri)) {
      return Result.error("数据异常!");
    }
    return topicService.findTagsByTopicUri(new Page<>(page, rows), topicUri);
  }

  @GetMapping("/topic/detail/{idTopic}")
  public Result<Topic> topicDetail(@PathVariable Integer idTopic) {
    return Result.OK(topicService.getById(idTopic.toString()));
  }

  @GetMapping("/topic/unbind-topic-tags")
  public Result<?> unbindTopicTags(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows,
      @RequestParam Integer idTopic,
      @RequestParam(required = false) String tagTitle) {
    return Result.OK(topicService.findUnbindTagsById(new Page<>(page, rows), idTopic, tagTitle));
  }

  @PostMapping("/topic/bind-topic-tag")
  public Result<?> bindTopicTag(@RequestBody TopicTagDTO topicTag) {
    return topicService.bindTopicTag(topicTag);
  }

  @DeleteMapping("/topic/unbind-topic-tag")
  public Result<?> unbindTopicTag(@RequestBody TopicTagDTO topicTag) {
    return topicService.unbindTopicTag(topicTag);
  }

  @RequestMapping(
      value = "/topic/post",
      method = {RequestMethod.PUT, RequestMethod.POST})
  public Result<?> addTopic(@RequestBody Topic topic) {
    return topicService.saveTopic(topic);
  }

  @GetMapping("/tags")
  public Result<IPage<Tag>> tags(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows) {
    return Result.OK(tagService.page(new Page<>(page, rows)));
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
