package com.example.blueberrypieapi.service.serviceImpl;

import com.example.blueberrypieapi.entity.LoginLog;
import com.example.blueberrypieapi.mapper.LoginLogMapper;
import com.example.blueberrypieapi.service.LoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cheng
 * @since 2020-04-22
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

}
