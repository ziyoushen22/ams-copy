package com.example.amscopy.utils;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

public class CodeGenerator {
    public static void main(String[] args) throws InterruptedException {
        AutoGenerator mpg = new AutoGenerator();
        // 选择 freemarker 引擎，默认 Veloctiy
//        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
//        String projectPath = System.getProperty("user.dir");
//        gc.setOutputDir(projectPath + "/src/main/java");
//        String projectPath = "D://ProjectCode/CodeGenerator";
        String projectPath = "D://ProjectCode/cc/newcc-springboot-provider/vm-provider";
        gc.setOutputDir(projectPath + "/src/main/java");
        // 是否覆盖同名文件，默认是false
        gc.setFileOverride(true);
        // 不需要ActiveRecord特性的请改为false
        gc.setActiveRecord(true);
        // XML 二级缓存
        gc.setEnableCache(false);
        // XML ResultMap
        gc.setBaseResultMap(true);
        // XML columList
        gc.setBaseColumnList(true);
        gc.setOpen(false);
        gc.setAuthor("mabingyang");
        gc.setSwagger2(true);
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("%sDao");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");
        gc.setDateType(DateType.ONLY_DATE);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUrl("jdbc:mysql://10.241.230.102:3306/DB_CLOUDCORE?useSSL=false");
        dsc.setUsername("root");
        dsc.setPassword("singularity0618");
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // 全局大写命名 ORACLE 注意
        // strategy.setCapitalMode(true);
        //此处可以修改为您的表前缀
//        strategy.setTablePrefix(new String[] { "tb_"});
        // 表名生成策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 需要生成的表
        strategy.setInclude(new String[]{"MAC_INFO"});
        // 排除生成的表
        //strategy.setExclude(new String[]{"test"});
        strategy.setEntityLombokModel(true);
        //控制器类上加上@RestController
        strategy.setRestControllerStyle(true);
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
//        pc.setParent("com.biggeryun.vm.provider");
        pc.setParent("");
        pc.setController("com.biggeryun.vm.provider.controller");
        pc.setService("com.biggeryun.vm.provider.service");
        pc.setServiceImpl("com.biggeryun.vm.provider.service.impl");
        pc.setMapper("com.biggeryun.vm.provider.dao");
        //后面有自定义路径
//        pc.setXml("mapper");
        pc.setEntity("com.biggeryun.common.entity");
//        pc.setModuleName("test");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        //自定义调整 xml 生成目录
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + "/src/main/resources/mapper/"
                        + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        //自定义调整实体类生成目录
        focList.add(new FileOutConfig("/templates/entity.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return "D:/ProjectCode/cc/newcc-common/src/main/java/com/biggeryun/common/entity/"
//                return "D:/ProjectCode/CodeGenerator/cc/newcc-common/src/main/java/com/biggeryun/common/entity/"
                        + tableInfo.getEntityName() + StringPool.DOT_JAVA;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        //设置模板
        TemplateConfig tc = new TemplateConfig();
        tc.setXml(null)
                .setEntity(null)
                .setController("templates/controller.java.vm")
                .setMapper("templates/mapper.java.vm")
                .setServiceImpl("templates/serviceImpl.java.vm")
                .setService("templates/service.java.vm");
        mpg.setTemplate(tc);
        // 执行生成
        mpg.execute();
    }
}
