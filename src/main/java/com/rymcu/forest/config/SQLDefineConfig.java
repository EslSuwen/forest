package com.rymcu.forest.config;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** @author suwen */
@Configuration
public class SQLDefineConfig {

  @Value("${spring.datasource.url:}")
  private String url;

  @Value("${spring.datasource.username:}")
  private String username;

  @Value("${spring.datasource.password:}")
  private String password;

  private Connection con;

  public Connection getCon() {
    return con;
  }

  public void getConnection() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      System.out.println("数据库驱动加载成功");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    try {
      con = DriverManager.getConnection(url, username, password);
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
    if (StrUtil.isBlank(url) || StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
      System.out.println("数据库连接信息未配置，跳过表存在检查");
      return;
    }
    getConnection();
    judgeTableExist();
  }
}
