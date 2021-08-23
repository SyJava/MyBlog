package com.example.blog.controller;


import com.example.blog.service.CountService;
import com.example.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sy
 * @since 2021-08-22
 */
@RestController
public class CountController {
    @Autowired
    CountService countService;
    @GetMapping("/getCount")
    public Result getCount(){
    return countService.getCount();
}
}

