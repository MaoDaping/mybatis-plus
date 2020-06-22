package com.maoda.mp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maoda.mp.entity.Employee;
import org.springframework.stereotype.Component;

/**
 * @Author msp
 * @Date 2020/6/17 19:14
 * @Description
 */
@Component("userMapper")
public interface UserMapper extends BaseMapper<Employee> {
}
