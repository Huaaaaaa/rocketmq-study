package com.study.rocketmq.service.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author: Hu-aaa-aaa
 * @createTime: 2022/10/7 10:00 下午
 * @description:
 */
@Service
@Slf4j
public class MessageConsumerServiceImpl implements MessageConsumerService {
    private static ClientServiceProvider provider;
    private static ClientConfiguration clientConfiguration;
    //接入点地址，需要设置成Proxy的地址和端口列表，一般是xxx:8081;xxx:8081。
    private static String endpoint = "localhost:8081";
    //为消费者指定所属的消费者分组，Group需要提前创建。
    private static String consumerGroup = "rocketmqStudy";
    //订阅消息的过滤规则，表示订阅所有Tag的消息。
    private static String tag = "*";

    static {
        provider = ClientServiceProvider.loadService();
        clientConfiguration = ClientConfiguration.newBuilder()
                .setEndpoints(endpoint)
                .build();
    }

    /**
     * 指定需要订阅哪个目标Topic，Topic需要提前创建。
     *
     * @param topic
     * @return
     */
    private PushConsumer getPushConsumer(String topic) throws ClientException {
        FilterExpression filterExpression = new FilterExpression(tag, FilterExpressionType.TAG);
        //初始化PushConsumer，需要绑定消费者分组ConsumerGroup、通信参数以及订阅关系。
        PushConsumer pushConsumer = provider.newPushConsumerBuilder()
                .setClientConfiguration(clientConfiguration)
                //设置消费者分组。
                .setConsumerGroup(consumerGroup)
                //设置预绑定的订阅关系。
                .setSubscriptionExpressions(Collections.singletonMap(topic, filterExpression))
                //设置消费监听器。
                .setMessageListener(messageView -> {
                    //处理消息并返回消费结果。
                    log.info("getPullConsumer->topic={},message={}", topic, messageView);
                    // LOGGER.info("Consume message={}", messageView);
                    System.out.println("Consume message!!");
                    return ConsumeResult.SUCCESS;
                }).build();
        return pushConsumer;
    }

    @Override
    public void consumerByPull(String topic) {

    }

    @Override
    public void consumerByPush(String topic) {
        try {
            String consumerGroup = getPushConsumer(topic).getConsumerGroup();
            log.info("consumerByPush consumerGroup={}", consumerGroup);
            Thread.sleep(Long.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //如果不需要再使用PushConsumer，可关闭该进程。
        //pushConsumer.close();
    }
}
