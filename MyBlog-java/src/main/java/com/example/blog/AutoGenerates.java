package com.example.blog;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class AutoGenerates {
    public static void main(String[] args) {
        //代码生成器
        AutoGenerator au = new AutoGenerator();
        //全局配置
        GlobalConfig gc = new GlobalConfig();
        String path = System.getProperty("user.dir");//获取项目磁盘地址
        gc.setOutputDir(path+"/src/main/java");//文件生成的地址
        gc.setEntityName("%s");//设置生成实体类的名称，%s对应库中的表名
        gc.setMapperName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setAuthor("sy");//设置作者
        gc.setIdType(IdType.AUTO);//设置主键id的配置
        au.setGlobalConfig(gc);//实现全局配置

        //设置数据源
        DataSourceConfig ds=new DataSourceConfig();
        //驱动
        ds.setDriverName("com.mysql.cj.jdbc.Driver");
        //设置Url
        ds.setUrl("jdbc:mysql://localhost:3306/myblog?serverTimezone=UTC");
        //设置数据库的用户名和密码
        ds.setUsername("root");
        ds.setPassword("shuai123");
        au.setDataSource(ds);//配置数据源

        //设置包信息
        PackageConfig pc = new PackageConfig();
        //设置模块名称
        pc.setModuleName("blog");
        //设置父包名，order在该包下
        pc.setParent("com.example");
        au.setPackageInfo(pc);

        //设置策略
        StrategyConfig sc = new StrategyConfig();
        sc.setColumnNaming(NamingStrategy.underline_to_camel);//设置字段驼峰命名规则
        sc.setNaming(NamingStrategy.underline_to_camel);//设置表名驼峰命名规则
        au.setStrategy(sc);
        //执行代码生成
        au.execute();
    }
}
