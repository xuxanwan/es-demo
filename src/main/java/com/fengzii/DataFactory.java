package com.fengzii;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

public class DataFactory {
    public static DataFactory dataFactory = new DataFactory();

    private DataFactory() {
    }

    public DataFactory getInstance() {
        return dataFactory;
    }

    public static List<String> getInitJsonData() {
        List<String> list = new ArrayList<String>();
        String data1 = JSON.toJSONString(new Blog(1, "csdn git简介", "2016-06-19", "Git是一款免费、开源的分布式版本控制系统，用于敏捷高效地处理任何或小或大的项目。 "));
        String data2 = JSON.toJSONString(new Blog(2, "csdn Java中泛型的介绍与简单使用", "2016-06-19", " git现在开始深入学习Java的泛型了，以前一直只是在集合中简单的使用泛型，根本就不明白泛型的原理和作用。"));
        String data3 = JSON.toJSONString(new Blog(3, "csdn SQL基本操作", "2016-06-19", "日期函数  "));
        String data4 = JSON.toJSONString(new Blog(4, "csdn Hibernate框架基础", "2016-06-19","hibernate是一个基于jdbc的开源的持久化框架，是一个优秀的ORM实现，它很大程度的简化了dao层编码工作。Hibernate对JDBC访问数据库的代码做了封装，"));
        String data5 = JSON.toJSONString(new Blog(5, "csdn Shell基本知识", "2016-06-19", "日常的linux系统管理工作中必不可少的就是shell脚本，如果不会写shell脚本，那么你就不算一个合格的管理员。"));
        String data6 = JSON.toJSONString(new Blog(6, "git", "2016-06-19", "git"));
        list.add(data1);
        list.add(data2);
        list.add(data3);
        list.add(data4);
        list.add(data5);
        list.add(data6);
        return list;
    }

}