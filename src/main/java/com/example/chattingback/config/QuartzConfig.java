package com.example.chattingback.config;

/**
 * @author Echim9
 * @date 2022/10/20 15:32
 */

import com.example.chattingback.Resource.RedisToMysqlTask;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Quartz定时任务配置
 */
@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail RedisToMysqlQuartz() {
        // 执行定时任务
        return JobBuilder.newJob(RedisToMysqlTask.class).withIdentity("CallPayQuartzTask").storeDurably().build();
    }

    @Bean
    public Trigger CallPayQuartzTaskTrigger() {
        //cron方式，从每月1号开始，每隔三天就执行一次
        return TriggerBuilder.newTrigger().forJob(RedisToMysqlQuartz())
                .withIdentity("CallPayQuartzTask")
                .withSchedule(CronScheduleBuilder.cronSchedule("* * 4 1/3 * ?"))
                .build();
    }
}
