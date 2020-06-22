package com.maoda.mp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.maoda.mp.dao.UserMapper;
import com.maoda.mp.entity.Employee;
import com.sun.javafx.logging.PulseLogger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.lang.model.type.ReferenceType;
import javax.naming.Name;
import javax.print.attribute.standard.Copies;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @Author msp
 * @Date 2020/6/17 19:46
 * @Description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MpTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void insert(){
        Employee employee = new Employee();
        employee.setName("刘大明");
        employee.setAge(30);
        employee.setManagerId(1087982257332887553L);
        employee.setCreateTime(LocalDateTime.now());
        int rows = userMapper.insert(employee);
        System.out.println(rows);
        userMapper.selectById(1273229149704896514L);
    }

    @Test
    public void select(){
        List<Employee> employees = userMapper.selectList(null);
        employees.forEach(System.out::println);
    }

    /**
     * 查询名字中包含'雨'并且年龄小于40
     * name like '%雨%' and age < 40
     */
    @Test
    public void selectByWrapper1(){
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "雨").lt("age", 40);
        List<Employee> employees = userMapper.selectList(queryWrapper);
        employees.forEach(System.out::println);
    }

    /**
     * 创建日期为2019年2月14日并且直属上级姓名为王姓
     * date_format(create_time, "%Y-%m-%d") and manager_id in (select id from uuu_test where name like '王%')
     */
    @Test
    public void selectByWrapper2(){
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("date_format(create_time, '%Y-%m-%d') = {0}", "2019-02-14")
                .inSql("manager_id", "select id from uuu_test where name like '王%'");
        List<Employee> employees = userMapper.selectList(queryWrapper);
        employees.forEach(System.out::println);
    }

    /**
     * 名字为王姓且（年龄小于40或者邮箱不为空）
     * name like '王%' and (age < 40 and mail is not null)
     */
    @Test
    public void selectByWrapper3(){
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("name", "王")
                .and(qw -> qw.lt("age", 40).or().isNotNull("email"));
        List<Employee> employees = userMapper.selectList(queryWrapper);
        employees.forEach(System.out::println);
    }

    /**
     * 名字为王姓，（年龄小于40大于20，并且邮箱不为空）
     * name like '王%' and (age between 20 and 40 and (email is not null))
     */
    @Test
    public void selectByWrapper4(){
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
//        queryWrapper.likeRight("name", "王")
//                .and(qw -> qw.between("age", 20, 40).isNotNull("email"));
        queryWrapper.nested(qw -> qw.between("age", 20, 40).isNotNull("email"))
                .likeRight("name", "王");
        List<Employee> employees = userMapper.selectList(queryWrapper);
        employees.forEach(System.out::println);
    }

    /**
     * （年龄小于40或者邮箱不为空）并且名字为王姓
     * (age < 40 or email is not null) and name like '王%'
     */
    @Test
    public void selectByWrapper5(){
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("name", "王")
                .and(qw -> qw.lt("age", 40).or().isNotNull("email"));
//        queryWrapper.nested(qw -> qw.lt("age", 40).or().isNotNull("email"))
//                .likeRight("name", "王");
        List<Employee> employees = userMapper.selectList(queryWrapper);
        employees.forEach(System.out::println);
    }

    /**
     * 年龄为30,31,35,34的员工
     * age in (30, 31, 35, 34)
     */
    @Test
    public void selectByWrapper6(){
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("age", Arrays.asList(30, 31, 35, 34));
        List<Employee> employees = userMapper.selectList(queryWrapper);
        employees.forEach(System.out::println);
    }

    /**
     * 年龄为30,31,35,34的员工中，最近加入系统的记录
     * age in(30, 31, 35, 34) order by create_time desc limit 1
     */
    @Test
    public void selectByWrapper7(){
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("age", Arrays.asList(30, 31, 35, 34))
                .last("order by create_time desc limit 1");
        List<Employee> employees = userMapper.selectList(queryWrapper);
        employees.forEach(System.out::println);
    }

    /**
     * 查找为王姓的员工的姓名和年龄
     * select name, age from uuu_test where name like '王%'；
     */
    @Test
    public void selectByWrapper8(){
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("name", "age")
                .likeRight("name", "王");
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    /**
     * 查询所有员工信息除了创建时间和主管ID列
     */
    @Test
    public void selectByWrapper(){
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(Employee.class, info -> !info.getColumn().equals("create_time") && !info.getColumn().equals("manager_id"));
        List<Employee> employees = userMapper.selectList(queryWrapper);
        employees.forEach(System.out::println);
    }
}
