package com.study.rocketmq.test.producer;

import com.study.rocketmq.service.producer.MessageProducerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.message.MessageId;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
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
public class ProducerTest {

    @Autowired
    private MessageProducerService messageProducerService;

    @Test
    public void testSendSync() {
        SendReceipt sendReceipt = messageProducerService.sendMsgSync("hyc_study_mq_topic", "msg1", "test", "wzp is xiao ke ai!");
        MessageId messageId = sendReceipt.getMessageId();
        log.info("testSendSync messageId={}", messageId);
    }
}
