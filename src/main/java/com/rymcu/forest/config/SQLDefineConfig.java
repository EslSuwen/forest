package com.rymcu.forest.config;

import cn.hutool.core.io.resource.ResourceUtil;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 知识库模块数据库自检配置
 *
 * @author suwen
 */
@Configuration
public class SQLDefineConfig {

  private Connection con;

  @Resource private DataSource dataSource;

  public Connection getCon() {
    return con;
  }

  public void getConnection() {
    try {
      con = dataSource.getConnection();
      System.out.println("数据库连接成功");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void judgeTableExist() {
    try {
      con.createStatement().execute("select count(*) from forest_article");
    } catch (Exception e) {
      System.out.println("知识库相关表不存在，尝试自动创建");
      executeTableDefine();
    }
  }

  public void executeTableDefine() {
    try {
      ScriptRunner runner = new ScriptRunner(con);
      runner.setEscapeProcessing(false);
      runner.setAutoCommit(true);
      runner.runScript(ResourceUtil.getReader("forest-table.sql", StandardCharsets.UTF_8));
      System.out.println("知识库相关表创建成功");
    } catch (Exception e) {
      System.out.println("知识库相关表创建失败，请手动创建");
      e.printStackTrace();
    }
  }

  @PostConstruct
  public void autoCreateTable() {
    System.out.println("开始数据库自检");
    if (dataSource == null) {
      System.out.println("数据库连接信息未配置，跳过表存在检查");
      return;
    }
    getConnection();
    judgeTableExist();
  }
}
