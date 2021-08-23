package com.example.blog.controller;


import com.example.blog.entity.Type;
import com.example.blog.service.TypeService;
import com.example.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sy
 * @since 2021-08-07
 */
@RestController
@RequestMapping("/type")
public class TypeController {
    @Autowired
    TypeService typeService;
/**
 * 查询全部分类
 */
@GetMapping("/getAllType")
public Result getAllType(){
    return typeService.getAllType();
}
    /**
     * 分页查询
     * @param current
     * @param size
     * @return
     */
    @GetMapping("/getTypeByPage")
    public Result getTypeByPage(Long current,Long size){
    return typeService.getTypeByPage(current,size);
    }
    /**
     * 添加分类
     * @param name
     * @return
     */
    @PostMapping("/addType")
    public Result addType(@RequestBody String name){ return typeService.addType(name); }

    @PutMapping("/updateType")
    public Result updateType(@RequestBody Type type){
        System.out.println(type);
        return typeService.updateType(type);
    }
    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping("/deleteTypeById")
    public Result deleteTypeById(Integer id){ return typeService.deleteType(id);}

}

