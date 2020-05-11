package com.example.blueberrypieapi.service.serviceImpl;

import com.example.blueberrypieapi.entity.Tag;
import com.example.blueberrypieapi.mapper.TagMapper;
import com.example.blueberrypieapi.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cheng
 * @since 2020-04-23
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

}
