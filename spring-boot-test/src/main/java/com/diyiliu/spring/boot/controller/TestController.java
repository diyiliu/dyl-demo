package com.diyiliu.spring.boot.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Description: TestController
 * Author: DIYILIU
 * Update: 2020-11-12 11:21
 */

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/hello")
    public String getTest(String name) {
        System.out.println(name);

        return "hello " + name;
    }

    @PostMapping("/post")
    public String postTest(String name) {
        System.out.println(name);

        return "hello " + name;
    }

    @PostMapping("/json")
    public String withJson(@RequestBody Map map) {
        String name = (String) map.get("name");

        System.out.println(name);

        return "hello " + name;
    }
}
