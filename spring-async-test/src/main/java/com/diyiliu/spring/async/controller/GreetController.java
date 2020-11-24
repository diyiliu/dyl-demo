package com.diyiliu.spring.async.controller;

import com.diyiliu.spring.async.model.AsyncMailTrigger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Description: GreetController
 * Author: DIYILIU
 * Update: 2020-11-24 09:18
 */

@RestController
public class GreetController {

    private AsyncMailTrigger greeter;

    public GreetController(AsyncMailTrigger greeter) {
        this.greeter = greeter;
    }

    @GetMapping("/greet")
    public String greet(HttpServletRequest request) throws Exception {
        String name = request.getParameter("name");
        greeter.asyncGreet(request);

        System.out.println(Thread.currentThread() + " Says Name is " + name);
        System.out.println(Thread.currentThread().getName() + " Hashcode" + request.hashCode());

        return name;
    }
}
