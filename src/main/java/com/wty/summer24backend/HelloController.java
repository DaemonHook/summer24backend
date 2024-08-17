package com.wty.summer24backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping(value = "/getTest", method = RequestMethod.GET)
    public String getTest(String name, String phone) {
        System.out.println("name: " + name);
        System.out.println("phone: " + phone);
        return "GET请求";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }
}
