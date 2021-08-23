package com.example.blog.controller;


import com.example.blog.entity.Tag;
import com.example.blog.service.TagService;
import com.example.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sy
 * @since 2021-08-12
 */
@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    TagService tagService;
    /**
     * 查询全部标签
     */
    @GetMapping("/getAllTag")
    public Result getAllTag(){
        return tagService.getAllTag();
    }
    /**
     * 分页查询
     * @param current
     * @param size
     * @return
     */
    @GetMapping("/getTagByPage")
    public Result getTagByPage(Long current,Long size){
        return tagService.getTagByPage(current,size);
    }
    /**
     * 添加标签
     * @param name
     * @return
     */
    @PostMapping("/addTag")
    public Result addTag(@RequestBody String name){ return tagService.addTag(name); }

    /**
     * 更新标签
     * @param tag
     * @return
     */
    @PutMapping("/updateTag")
    public Result updateTag(@RequestBody Tag tag){
        System.out.println(tag);
        return tagService.updateTag(tag);
    }
    /**
     * 删除标签
     * @param id
     * @return
     */
    @DeleteMapping("/deleteTagById")
    public Result deleteTagById(Integer id){ return tagService.deleteTag(id);}

}

