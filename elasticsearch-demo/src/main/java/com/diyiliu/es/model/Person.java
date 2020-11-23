package com.diyiliu.es.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Description: Person
 * Author: DIYILIU
 * Update: 2020-11-23 15:59
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    private int age;

    private String fullName;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date dateOfBirth;
}
