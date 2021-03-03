package com.rymcu.forest.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/** @author suwen */
@Configuration
@MapperScan({"com.rymcu.forest.mapper", "com.rymcu.forest.lucene.mapper"})
public class MapperConfig {}
