package com.maoda.mp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author msp
 * @Date 2020/6/17 19:07
 * @Description
 */
@Data
@TableName(value = "uuu_test")
public class Employee {

    /** 主键 **/
    private Long id;

    /** 姓名 **/
    private String name;

    /** 年龄 **/
    private Integer age;

    /** 邮箱 **/
    private String email;

    /** 直属上级id */
    private Long managerId;

    /** 创建时间 */
    private LocalDateTime createTime;

}
