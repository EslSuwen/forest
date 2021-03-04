/*
 Navicat Premium Data Transfer

 Source Server         : aliyun
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : 47.115.8.44:3306
 Source Schema         : forest

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 04/03/2021 15:48:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for forest_article
-- ----------------------------
DROP TABLE IF EXISTS `forest_article`;
CREATE TABLE `forest_article` (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                  `article_title` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文章标题',
                                  `article_thumbnail_url` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文章缩略图',
                                  `article_author_id` bigint(20) DEFAULT NULL COMMENT '文章作者id',
                                  `article_type` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '文章类型',
                                  `article_tags` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文章标签',
                                  `article_view_count` int(11) DEFAULT '1' COMMENT '浏览总数',
                                  `article_preview_content` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '预览内容',
                                  `article_comment_count` int(11) DEFAULT '0' COMMENT '评论总数',
                                  `article_permalink` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文章永久链接',
                                  `article_link` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '站内链接',
                                  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
                                  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
                                  `article_perfect` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '0:非优选1：优选',
                                  `article_status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '文章状态',
                                  `article_thumbs_up_count` int(11) DEFAULT '0' COMMENT '点赞总数',
                                  `article_sponsor_count` int(11) DEFAULT '0' COMMENT '赞赏总数',
                                  `deleted` int(11) DEFAULT '0',
                                  PRIMARY KEY (`id`),
                                  UNIQUE KEY `key` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11568 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT=' ';

-- ----------------------------
-- Table structure for forest_article_content
-- ----------------------------
DROP TABLE IF EXISTS `forest_article_content`;
CREATE TABLE `forest_article_content` (
                                          `id_article` bigint(20) NOT NULL COMMENT '主键',
                                          `article_content` text COLLATE utf8mb4_unicode_ci COMMENT '文章内容原文',
                                          `article_content_html` text COLLATE utf8mb4_unicode_ci COMMENT '文章内容Html',
                                          `created_time` datetime DEFAULT NULL COMMENT '创建时间',
                                          `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
                                          KEY `forest_article_content_id_article_index` (`id_article`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT=' ';

-- ----------------------------
-- Table structure for forest_article_thumbs_up
-- ----------------------------
DROP TABLE IF EXISTS `forest_article_thumbs_up`;
CREATE TABLE `forest_article_thumbs_up` (
                                            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                            `id_article` bigint(20) DEFAULT NULL COMMENT '文章表主键',
                                            `id_user` bigint(20) DEFAULT NULL COMMENT '用户表主键',
                                            `thumbs_up_time` datetime DEFAULT NULL COMMENT '点赞时间',
                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章点赞表 ';

-- ----------------------------
-- Table structure for forest_bank
-- ----------------------------
DROP TABLE IF EXISTS `forest_bank`;
CREATE TABLE `forest_bank` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                               `bank_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '银行名称',
                               `bank_owner` bigint(20) DEFAULT NULL COMMENT '银行负责人',
                               `bank_description` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '银行描述',
                               `created_by` bigint(20) DEFAULT NULL COMMENT '创建人',
                               `created_time` datetime DEFAULT NULL COMMENT '创建时间',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='银行表 ';

-- ----------------------------
-- Table structure for forest_bank_account
-- ----------------------------
DROP TABLE IF EXISTS `forest_bank_account`;
CREATE TABLE `forest_bank_account` (
                                       `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                       `id_bank` bigint(20) DEFAULT NULL COMMENT '所属银行',
                                       `bank_account` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '银行账户',
                                       `account_balance` decimal(32,8) DEFAULT NULL COMMENT '账户余额',
                                       `account_owner` bigint(20) DEFAULT NULL COMMENT '账户所有者',
                                       `created_time` datetime DEFAULT NULL COMMENT '创建时间',
                                       `account_type` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '0: 普通账户 1: 银行账户',
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='银行账户表 ';

-- ----------------------------
-- Table structure for forest_comment
-- ----------------------------
DROP TABLE IF EXISTS `forest_comment`;
CREATE TABLE `forest_comment` (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                  `comment_content` text COLLATE utf8mb4_unicode_ci COMMENT '评论内容',
                                  `comment_author_id` bigint(20) DEFAULT NULL COMMENT '作者 id',
                                  `comment_article_id` bigint(20) DEFAULT NULL COMMENT '文章 id',
                                  `comment_sharp_url` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '锚点 url',
                                  `comment_original_comment_id` bigint(20) DEFAULT NULL COMMENT '父评论 id',
                                  `comment_status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '状态',
                                  `comment_ip` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '评论 IP',
                                  `comment_ua` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'User-Agent',
                                  `comment_anonymous` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '0：公开回帖，1：匿名回帖',
                                  `comment_reply_count` int(11) DEFAULT NULL COMMENT '回帖计数',
                                  `comment_visible` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '0：所有人可见，1：仅楼主和自己可见',
                                  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表 ';

-- ----------------------------
-- Table structure for forest_currency_issue
-- ----------------------------
DROP TABLE IF EXISTS `forest_currency_issue`;
CREATE TABLE `forest_currency_issue` (
                                         `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                         `issue_value` decimal(32,8) DEFAULT NULL COMMENT '发行数额',
                                         `created_by` bigint(20) DEFAULT NULL COMMENT '发行人',
                                         `created_time` datetime DEFAULT NULL COMMENT '发行时间',
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='货币发行表 ';

-- ----------------------------
-- Table structure for forest_currency_rule
-- ----------------------------
DROP TABLE IF EXISTS `forest_currency_rule`;
CREATE TABLE `forest_currency_rule` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                        `rule_name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '规则名称',
                                        `rule_sign` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '规则标志(与枚举变量对应)',
                                        `rule_description` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '规则描述',
                                        `money` decimal(32,8) DEFAULT NULL COMMENT '金额',
                                        `award_status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '奖励(0)/消耗(1)状态',
                                        `maximum_money` decimal(32,8) DEFAULT NULL COMMENT '上限金额',
                                        `repeat_days` int(11) DEFAULT '0' COMMENT '重复(0: 不重复,单位:天)',
                                        `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '状态',
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='货币规则表 ';

-- ----------------------------
-- Table structure for forest_follow
-- ----------------------------
DROP TABLE IF EXISTS `forest_follow`;
CREATE TABLE `forest_follow` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                 `follower_id` bigint(20) DEFAULT NULL COMMENT '关注者 id',
                                 `following_id` bigint(20) DEFAULT NULL COMMENT '关注数据 id',
                                 `following_type` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '0：用户，1：标签，2：帖子收藏，3：帖子关注',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='关注表 ';

-- ----------------------------
-- Table structure for forest_notification
-- ----------------------------
DROP TABLE IF EXISTS `forest_notification`;
CREATE TABLE `forest_notification` (
                                       `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                       `id_user` bigint(20) DEFAULT NULL COMMENT '用户id',
                                       `data_type` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据类型',
                                       `data_id` bigint(20) DEFAULT NULL COMMENT '数据id',
                                       `has_read` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '是否已读',
                                       `data_summary` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据摘要',
                                       `created_time` datetime DEFAULT NULL COMMENT '创建时间',
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知表 ';

-- ----------------------------
-- Table structure for forest_portfolio
-- ----------------------------
DROP TABLE IF EXISTS `forest_portfolio`;
CREATE TABLE `forest_portfolio` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                    `portfolio_head_img_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '作品集头像',
                                    `portfolio_title` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '作品集名称',
                                    `portfolio_author_id` bigint(20) DEFAULT NULL COMMENT '作品集作者',
                                    `portfolio_description` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '作品集介绍',
                                    `created_time` datetime DEFAULT NULL COMMENT '创建时间',
                                    `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
                                    `portfolio_description_html` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT ' 作品集介绍HTML',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='作品集表';

-- ----------------------------
-- Table structure for forest_portfolio_article
-- ----------------------------
DROP TABLE IF EXISTS `forest_portfolio_article`;
CREATE TABLE `forest_portfolio_article` (
                                            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                            `id_portfolio` bigint(20) DEFAULT NULL COMMENT '作品集表主键',
                                            `id_article` bigint(20) DEFAULT NULL COMMENT '文章表主键',
                                            `sort_no` int(11) DEFAULT NULL COMMENT '排序号',
                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='作品集与文章关系表';

-- ----------------------------
-- Table structure for forest_role
-- ----------------------------
DROP TABLE IF EXISTS `forest_role`;
CREATE TABLE `forest_role` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                               `name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
                               `input_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '拼音码',
                               `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '状态',
                               `created_time` datetime DEFAULT NULL COMMENT '创建时间',
                               `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
                               `weights` tinyint(4) DEFAULT '0' COMMENT '权重,数值越小权限越大 0:无权限',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT=' ';
INSERT INTO `forest_role` VALUES (1, '管理员', 'admin', '0', '2019-11-16 04:22:45', '2019-11-16 04:22:45', 1);
INSERT INTO `forest_role` VALUES (2, '社区管理员', 'blog_admin', '0', '2019-12-05 03:10:05', '2019-12-05 17:11:35', 2);
INSERT INTO `forest_role` VALUES (3, '作者', 'zz', '0', '2020-03-12 15:07:27', '2020-03-12 15:07:27', 3);
INSERT INTO `forest_role` VALUES (4, '普通用户', 'user', '0', '2019-12-05 03:10:59', '2020-03-12 15:13:49', 4);

-- ----------------------------
-- Table structure for forest_sponsor
-- ----------------------------
DROP TABLE IF EXISTS `forest_sponsor`;
CREATE TABLE `forest_sponsor` (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                  `data_type` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据类型',
                                  `data_id` bigint(20) DEFAULT NULL COMMENT '数据主键',
                                  `sponsor` bigint(20) DEFAULT NULL COMMENT '赞赏人',
                                  `sponsorship_time` datetime DEFAULT NULL COMMENT '赞赏日期',
                                  `sponsorship_money` decimal(32,8) DEFAULT NULL COMMENT '赞赏金额',
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='赞赏表 ';

-- ----------------------------
-- Table structure for forest_tag
-- ----------------------------
DROP TABLE IF EXISTS `forest_tag`;
CREATE TABLE `forest_tag` (
                              `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                              `tag_title` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签名',
                              `tag_icon_path` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签图标',
                              `tag_uri` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签uri',
                              `tag_description` text COLLATE utf8mb4_unicode_ci COMMENT '描述',
                              `tag_view_count` int(11) DEFAULT '0' COMMENT '浏览量',
                              `tag_article_count` int(11) DEFAULT '0' COMMENT '关联文章总数',
                              `tag_ad` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签广告',
                              `tag_show_side_ad` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否显示全站侧边栏广告',
                              `created_time` datetime DEFAULT NULL COMMENT '创建时间',
                              `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
                              `tag_status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '标签状态',
                              `tag_reservation` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '保留标签',
                              `tag_description_html` text COLLATE utf8mb4_unicode_ci,
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表 ';

-- ----------------------------
-- Table structure for forest_tag_article
-- ----------------------------
DROP TABLE IF EXISTS `forest_tag_article`;
CREATE TABLE `forest_tag_article` (
                                      `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                      `id_tag` bigint(20) DEFAULT NULL COMMENT '标签 id',
                                      `id_article` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '帖子 id',
                                      `article_comment_count` int(11) DEFAULT '0' COMMENT '帖子评论计数 0',
                                      `article_perfect` int(11) DEFAULT '0' COMMENT '0:非优选1：优选 0',
                                      `created_time` datetime DEFAULT NULL COMMENT '创建时间',
                                      `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
                                      PRIMARY KEY (`id`),
                                      KEY `forest_tag_article_id_tag_index` (`id_tag`)
) ENGINE=InnoDB AUTO_INCREMENT=126 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签 - 帖子关联表 ';

-- ----------------------------
-- Table structure for forest_topic
-- ----------------------------
DROP TABLE IF EXISTS `forest_topic`;
CREATE TABLE `forest_topic` (
                                `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                `topic_title` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '专题标题',
                                `topic_uri` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '专题路径',
                                `topic_description` text COLLATE utf8mb4_unicode_ci COMMENT '专题描述',
                                `topic_type` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '专题类型',
                                `topic_sort` int(11) DEFAULT '10' COMMENT '专题序号 10',
                                `topic_icon_path` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '专题图片路径',
                                `topic_nva` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '0：作为导航1：不作为导航 0',
                                `topic_tag_count` int(11) DEFAULT '0' COMMENT '专题下标签总数 0',
                                `topic_status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '0：正常1：禁用 0',
                                `created_time` datetime DEFAULT NULL COMMENT '创建时间',
                                `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
                                `topic_description_html` text COLLATE utf8mb4_unicode_ci COMMENT '专题描述 Html',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='主题表';

-- ----------------------------
-- Table structure for forest_topic_tag
-- ----------------------------
DROP TABLE IF EXISTS `forest_topic_tag`;
CREATE TABLE `forest_topic_tag` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                    `id_topic` bigint(20) DEFAULT NULL COMMENT '专题id',
                                    `id_tag` bigint(20) DEFAULT NULL COMMENT '标签id',
                                    `created_time` datetime DEFAULT NULL COMMENT '创建时间',
                                    `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
                                    PRIMARY KEY (`id`),
                                    KEY `forest_topic_tag_id_topic_index` (`id_topic`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='专题- 标签关联表 ';

-- ----------------------------
-- Table structure for forest_transaction_record
-- ----------------------------
DROP TABLE IF EXISTS `forest_transaction_record`;
CREATE TABLE `forest_transaction_record` (
                                             `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '交易主键',
                                             `transaction_no` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '交易流水号',
                                             `funds` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '款项',
                                             `form_bank_account` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '交易发起方',
                                             `to_bank_account` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '交易收款方',
                                             `money` decimal(32,8) DEFAULT NULL COMMENT '交易金额',
                                             `transaction_type` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '交易类型',
                                             `transaction_time` datetime DEFAULT NULL COMMENT '交易时间',
                                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交易记录表 ';

-- ----------------------------
-- Table structure for forest_user
-- ----------------------------
DROP TABLE IF EXISTS `forest_user`;
CREATE TABLE `forest_user` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                               `account` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '账号',
                               `password` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
                               `nickname` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '昵称',
                               `real_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '真实姓名',
                               `sex` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '性别',
                               `avatar_type` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '头像类型',
                               `avatar_url` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像路径',
                               `email` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
                               `phone` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
                               `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '状态',
                               `created_time` datetime DEFAULT NULL COMMENT '创建时间',
                               `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
                               `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
                               `signature` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '签名',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT=' ';
INSERT INTO `forest_user` VALUES (1, 'admin', '8ce2dd866238958ac4f07870766813cdaa39a9b83a8c75e26aa50f23', 'admin', 'admin', '0', '0', NULL, NULL, NULL, '0', '2021-01-25 18:21:51', '2021-01-25 18:21:54', '2021-03-04 14:37:20', NULL);

-- ----------------------------
-- Table structure for forest_user_extend
-- ----------------------------
DROP TABLE IF EXISTS `forest_user_extend`;
CREATE TABLE `forest_user_extend` (
                                      `id_user` bigint(20) NOT NULL COMMENT '用户表主键',
                                      `github` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'github',
                                      `weibo` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微博',
                                      `weixin` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信',
                                      `qq` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'qq',
                                      `blog` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '博客'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户扩展表 ';
INSERT INTO `forest_user_extend` VALUES (1, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for forest_user_role
-- ----------------------------
DROP TABLE IF EXISTS `forest_user_role`;
CREATE TABLE `forest_user_role` (
                                    `id_user` bigint(20) NOT NULL COMMENT '用户表主键',
                                    `id_role` bigint(20) NOT NULL COMMENT '角色表主键',
                                    `created_time` datetime DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT=' ';
INSERT INTO `forest_user_role` VALUES (1, 1, '2021-01-25 18:22:12');

-- ----------------------------
-- Table structure for forest_user_tag
-- ----------------------------
DROP TABLE IF EXISTS `forest_user_tag`;
CREATE TABLE `forest_user_tag` (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                   `id_user` bigint(20) DEFAULT NULL COMMENT '用户 id',
                                   `id_tag` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签 id',
                                   `type` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '0：创建者，1：帖子使用，2：用户自评标签',
                                   `created_time` datetime DEFAULT NULL COMMENT '创建时间',
                                   `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户 - 标签关联表 ';

-- ----------------------------
-- Table structure for forest_visit
-- ----------------------------
DROP TABLE IF EXISTS `forest_visit`;
CREATE TABLE `forest_visit` (
                                `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                `visit_url` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '浏览链接',
                                `visit_ip` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'IP',
                                `visit_ua` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'User-Agent',
                                `visit_city` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '城市',
                                `visit_device_id` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '设备唯一标识',
                                `visit_user_id` bigint(20) DEFAULT NULL COMMENT '浏览者 id',
                                `visit_referer_url` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '上游链接',
                                `created_time` datetime DEFAULT NULL COMMENT '创建时间',
                                `expired_time` datetime DEFAULT NULL COMMENT '过期时间',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5268 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='浏览表';

-- ----------------------------
-- Table structure for lucene_user_dic
-- ----------------------------
DROP TABLE IF EXISTS `lucene_user_dic`;
CREATE TABLE `lucene_user_dic` (
                                   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '字典编号',
                                   `dic` char(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '字典',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `lucene_user_dic_id_uindex` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户扩展字典';

SET FOREIGN_KEY_CHECKS = 1;
