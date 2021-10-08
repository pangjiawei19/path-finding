package com.mt.server.pathfinding.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pangjiawei
 * @created 2021-08-10 18:03:33
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "hello world !!";
    }

    @GetMapping("/test")
    public String test() {
        return "hello test !!";
    }
}
