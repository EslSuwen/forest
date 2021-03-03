package com.rymcu.forest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan({"com.rymcu.forest.mapper", "com.rymcu.forest.lucene.mapper"})
public class DemoParentApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoParentApplication.class, args);
  }
}
