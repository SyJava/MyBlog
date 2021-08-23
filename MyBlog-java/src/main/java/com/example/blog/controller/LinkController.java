package com.example.blog.controller;


import com.example.blog.entity.Link;
import com.example.blog.service.LinkService;
import com.example.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sy
 * @since 2021-08-18
 */
@RestController
@RequestMapping("/links")
public class LinkController {
    @Autowired
    LinkService linkService;
    @PostMapping("/saveLink")
    public Result saveLink(@RequestBody Link link){
    return linkService.saveLink(link);
}
    @GetMapping("/getLinksByPage")
    public Result getLinksByPage(Long current,Long size){
    return linkService.getLinksByPage(current,size);
}
    @PutMapping("/updateLink")
    public  Result updateLink(@RequestBody Link link){
    return linkService.updateLink(link);
}
    @DeleteMapping("/deleteLinkById")
    public  Result deleteLinkById(Integer id){
    return linkService.deleteLinkById(id);
}
    @GetMapping("/getAllLink")
    public Result getAllLink(){
    return linkService.getAllLink();
}
}

