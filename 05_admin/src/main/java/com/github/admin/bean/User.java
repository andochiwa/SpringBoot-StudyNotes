package com.github.admin.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author HAN
 * @version 1.0
 * @create 03-07-2:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user") // 指定表名
public class User {

    /**
     * @TableField(exist = false) 指定这个属性在表中不存在
     */
    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private String password;

    // 以下是数据库的字段
    private Integer id;
    private String name;
    private Integer age;
    private String email;

}
