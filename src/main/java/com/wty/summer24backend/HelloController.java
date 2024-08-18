package com.wty.summer24backend;

import com.wty.summer24backend.entity.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class HelloController {
    @RequestMapping(value = "/getTest", method = RequestMethod.GET)
    public String getTest(String name, String phone) {
        System.out.println("name: " + name);
        System.out.println("phone: " + phone);
        return "GET请求";
    }

    @GetMapping("/hello/*")
    public String hello() {
        return "Hello World";
    }

    @PostMapping(value = "/postTest")
    public String postTest(User user) {
        System.out.println(user);
        return "POST请求";
    }

    @PostMapping(value = "/postTest2")
    public String postTest2(@RequestBody User user) {
        System.out.println(user);
        return "POST请求2";
    }

    @RequestMapping(value = "/fileTest", method = RequestMethod.POST)
    public static boolean createOrUpdateMultipartFile(String folderPath, MultipartFile multipartFile) {
        if (null == multipartFile) {
            return false;
        }
        File folder = new File(folderPath);
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                return false;
            }
        }
        System.out.println("received file: " + multipartFile.getOriginalFilename());
        File file = new File(folder.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
