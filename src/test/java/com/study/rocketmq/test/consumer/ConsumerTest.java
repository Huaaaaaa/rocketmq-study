package com.study.rocketmq.test.consumer;

import com.study.rocketmq.service.consumer.MessageConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: Hu-aaa-aaa
 * @createTime: 2022/10/7 7:35 下午
 * @description:
 */
@SpringBootTest
@Slf4j
public class ConsumerTest {

    @Autowired
    private MessageConsumerService messageConsumerService;

    @Test
    public void testConsumeByPush() {
        messageConsumerService.consumerByPush("hyc_study_mq_topic");
    }
}
