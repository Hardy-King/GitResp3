package com.gk;

import com.gk.utils.Assistant;
import com.gk.utils.MyTools;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class AgentTest {
    public static void main(String[] args) {
        // --- 步骤 A: 初始化大模型 ---
        // 推荐使用 Builder 模式，更灵活
        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey("YOUR_OPENAI_API_KEY") // 替换成你的 Key，如 "sk-..."
                .modelName("gpt-4o")           // 或者 "gpt-3.5-turbo"
                .temperature(0.7)              // 创造力参数
                .build();

        // --- 步骤 B: 实例化工具 ---
        MyTools tools = new MyTools();

        // --- 步骤 C: 组装智能体 (核心！这里用的是最新的 AiServices.builder) ---

        /*Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(model)      // 绑定大脑
                .tool(tools)                   // 绑定工具（可以传对象，也可以传ToolProvider）
                // .memoryProvider(...)        // 如果需要记忆功能，在这里配置
                .build();*/
        Assistant assistant = null;

        // --- 步骤 D: 运行智能体 ---
        System.out.println("====== 智能体已启动 ======");

        // 测试1：查天气（会自动调用 getWeather 方法）
        String question1 = "帮我查一下北京的天气";
        String answer1 = assistant.chat(question1);
        System.out.println("用户: " + question1);
        System.out.println("Agent: " + answer1);
        System.out.println("--------------------------");

        // 测试2：算数（会自动调用 add 方法）
        String question2 = "123 加 456 等于多少？";
        String answer2 = assistant.chat(question2);
        System.out.println("用户: " + question2);
        System.out.println("Agent: " + answer2);
        System.out.println("--------------------------");

        // 测试3：混合提问
        String question3 = "上海的天气怎么样？如果温度加5度是多少？";
        String answer3 = assistant.chat(question3);
        System.out.println("用户: " + question3);
        System.out.println("Agent: " + answer3);
    }
}
