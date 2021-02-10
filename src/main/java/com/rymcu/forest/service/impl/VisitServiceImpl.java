package com.rymcu.forest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rymcu.forest.entity.Visit;
import com.rymcu.forest.mapper.VisitMapper;
import com.rymcu.forest.service.VisitService;
import org.springframework.stereotype.Service;

/** @author ronger */
@Service
public class VisitServiceImpl extends ServiceImpl<VisitMapper, Visit> implements VisitService {}
