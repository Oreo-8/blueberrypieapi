package com.example.blueberrypieapi.mapper;

import com.example.blueberrypieapi.entity.Reply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cheng
 * @since 2020-04-23
 */
public interface ReplyMapper extends BaseMapper<Reply> {

    List<Reply> getReplyByComment(@Param("id")String id);

}
