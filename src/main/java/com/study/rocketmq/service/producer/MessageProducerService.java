package com.study.rocketmq.service.producer;

import org.apache.rocketmq.client.apis.producer.SendReceipt;

/**
 * @author: Hu-aaa-aaa
 * @createTime: 2022/9/25 7:09 下午
 * @description:
 */
public interface MessageProducerService {

     SendReceipt sendMsgSync(String topic, String key, String tag, String msg);

     SendReceipt sendMsgAsync(String topic, String key, String tag, String msg);
}
