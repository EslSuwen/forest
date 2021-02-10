package com.rymcu.forest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.rymcu.forest.mapper", "com.rymcu.forest.lucene.mapper"})
public class ForestApplication {

  public static void main(String[] args) {
    SpringApplication.run(ForestApplication.class, args);
  }
}
