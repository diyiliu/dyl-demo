package com.diyiliu.spring.async.model;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Description: AsyncMailTrigger
 * Author: DIYILIU
 * Update: 2020-11-24 09:21
 */

@Component
public class AsyncMailTrigger {

    @Async
    public void asyncGreet(HttpServletRequest request) throws Exception {
        System.out.println("Trigger mail in a New Thread :: " + Thread.currentThread().getName());
        System.out.println(Thread.currentThread().getName() + " greets before sleep" + request.getParameter("name"));
        Thread.sleep(1000);
        System.out.println(Thread.currentThread().getName() + " greets" + request.getParameter("name"));
        System.out.println(Thread.currentThread().getName() + " Hashcode" + request.hashCode());
    }
}
