package com.datadog.test;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @RequestMapping(value = "/**")
    public String homePage(@RequestBody(required = false) String req) {
        return "hello world " + req;
    }
}
