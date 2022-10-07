package com.study.rocketmq.service.consumer;

/**
 * @author: Hu-aaa-aaa
 * @createTime: 2022/10/7 9:58 下午
 * @description:
 */
public interface MessageConsumerService {


     void consumerByPull(String topic);


    void consumerByPush(String topic);
}
