package com.gk.utils;

import dev.langchain4j.agent.tool.Tool;
// ==========================================
// 1. 定义工具类 (智能体的“手”)
// ==========================================
public class MyTools {
    @Tool("查询城市天气")
    public String getWeather(String city) {
        // 模拟真实API调用
        if ("北京".equals(city)) {
            return "北京今天晴，25度，空气质量优";
        } else if ("上海".equals(city)) {
            return "上海今天小雨，22度";
        } else {
            return "未知城市，无法查询";
        }
    }

    @Tool("计算两个数的和")
    public int add(int a, int b) {
        return a + b;
    }
}
