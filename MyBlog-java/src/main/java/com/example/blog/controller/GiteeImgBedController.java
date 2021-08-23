package com.example.blog.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.blog.utils.GiteeImgBedUtil;
import com.example.blog.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 公众号：饮亿杯Java
 *
 * @author SY
 * @date 2021/8/6 15:45
 */
@RestController
//使用gitee上传或删除图片
public class GiteeImgBedController {

        private final Logger logger = LoggerFactory.getLogger(this.getClass());

        /**
         * 上传图片
         * @param file
         * @return
         * @throws Exception
         */
        @PostMapping("/uploadImg")
        public Result uploadImg(@RequestParam("image") MultipartFile file) throws Exception {
            logger.info("请求成功");//打印到控制台
            String originaFileName = file.getOriginalFilename();
            //上传图片不存在时
            if(originaFileName == null){
                return Result.fail("图片不存在");
            }

            String suffix = originaFileName.substring(originaFileName.lastIndexOf("."));
            //设置图片名字
            String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + suffix;

            String paramImgFile = Base64.encode(file.getBytes());//base64编码
            //设置转存到Gitee仓库参数
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("access_token", GiteeImgBedUtil.ACCESS_TOKEN);
            paramMap.put("message", GiteeImgBedUtil.ADD_MESSAGE);
            paramMap.put("content", paramImgFile);

            //转存文件路径
            String targetDir = GiteeImgBedUtil.PATH + fileName;
            //设置请求路径
            String requestUrl = String.format(GiteeImgBedUtil.CREATE_REPOS_URL, GiteeImgBedUtil.OWNER,
                    GiteeImgBedUtil.REPO_NAME, targetDir);
            logger.info("请求Gitee仓库路径:{}",requestUrl);

            String resultJson = HttpUtil.post(requestUrl, paramMap);//发起post请求并返回信息
            JSONObject jsonObject = JSONUtil.parseObj(resultJson);
            //表示操作失败
            if (jsonObject==null || jsonObject.getObj("commit") == null) {
                return Result.fail("操作失败");
            }
            JSONObject content = JSONUtil.parseObj(jsonObject.getObj("content"));
            return Result.succ(content.getObj("download_url"));//返回图片地址
        }

        /**
         * 删除图片
         * @param imgPath
         * @return
         * @throws Exception
         */
        @DeleteMapping("/delImg")
        public Result delImg(@RequestParam("imgPath") String imgPath) throws Exception {
            //路径不存在时
            if(imgPath == null || imgPath.trim().equals("")){
                return Result.fail("图片路径不存在");
            }
            System.out.println(imgPath);
            String path = imgPath.split("master/")[1];
            //图片不存在时
            if(path == null || path.trim().equals("")){
                return Result.fail("图片不存在");
            }
            //设置请求路径
            String requestUrl = String.format(GiteeImgBedUtil.GET_IMG_URL, GiteeImgBedUtil.OWNER,
                    GiteeImgBedUtil.REPO_NAME, path);

            logger.info("请求Gitee仓库路径:{}",requestUrl);

            //获取图片所有信息
            String resultJson = HttpUtil.get(requestUrl);//发起get请求

            JSONObject jsonObject = JSONUtil.parseObj(resultJson);
            if (jsonObject == null) {
                logger.error("Gitee服务器未响应,message:{}",jsonObject);
                return Result.fail("服务器未响应");
            }
            //获取sha,用于删除图片
            String sha = jsonObject.getStr("sha");
            //设置删除请求参数
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("access_token", GiteeImgBedUtil.ACCESS_TOKEN);
            paramMap.put("sha", sha);
            paramMap.put("message", GiteeImgBedUtil.DEl_MESSAGE);
            //设置删除路径
            requestUrl = String.format(GiteeImgBedUtil.DEL_IMG_URL, GiteeImgBedUtil.OWNER,
                    GiteeImgBedUtil.REPO_NAME, path);
            logger.info("请求Gitee仓库路径:{}",requestUrl);
            //删除文件请求路径
            resultJson = HttpRequest.delete(requestUrl).form(paramMap).execute().body();
            HttpRequest.put(requestUrl).form(paramMap).execute().body();
            jsonObject = JSONUtil.parseObj(resultJson);
            //请求之后的操作
            if(jsonObject.getObj("commit") == null){
                logger.error("Gitee服务器未响应,message:{}",jsonObject);
                return Result.fail("服务器未响应");
            }
            return Result.succ("删除成功");
        }

    }



