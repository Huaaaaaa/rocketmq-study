package com.study.rocketmq.service.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientConfigurationBuilder;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author: Hu-aaa-aaa
 * @createTime: 2022/9/25 7:12 下午
 * @description:
 */
@Service
@Slf4j
public class MessageProducerServiceImpl implements MessageProducerService {


    //接入点地址，需要设置成Proxy的地址和端口列表，一般是xxx:8081;xxx:8081。
    private static String endpoint = "localhost:8081";
    private static ClientServiceProvider provider;
    private static ClientConfigurationBuilder builder;
    private static ClientConfiguration configuration;

    static {
        provider = ClientServiceProvider.loadService();
        builder = ClientConfiguration.newBuilder().setEndpoints(endpoint);
        configuration = builder.build();
    }

    private Producer getProducer(String topic) {
        //消息发送的目标Topic名称，需要提前创建:初始化Producer时需要设置通信配置以及预绑定的Topic。
        Producer producer = null;
        try {
            producer = provider.newProducerBuilder()
                    .setTopics(topic)
                    .setClientConfiguration(configuration)
                    .build();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return producer;
    }

    private Message getMessage(String topic, String key, String tag, String msg) {
        //普通消息发送。
        Message message = provider.newMessageBuilder()
                .setTopic(topic)
                //设置消息索引键，可根据关键字精确查找某条消息。
                .setKeys(key)
                //设置消息Tag，用于消费端根据指定Tag过滤消息。
                .setTag(tag)
                //消息体。
                .setBody(msg.getBytes())
                .build();
        log.info("getMessage message={}", message);
        return message;
    }

    @Override
    public SendReceipt sendMsgSync(String topic, String key, String tag, String msg) {
        log.info("sendMsgSync begin...");
        SendReceipt sendReceipt = null;
        try {
            //发送消息，需要关注发送结果，并捕获失败等异常。
            sendReceipt = getProducer(topic).send(getMessage(topic, key, tag, msg));
            System.out.println(sendReceipt.getMessageId());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        log.info("sendMsgSync end:sendReceipt={}", sendReceipt);
        return sendReceipt;
    }

    @Override
    public SendReceipt sendMsgAsync(String topic, String key, String tag, String msg) {
        log.info("sendMsgAsync begin...");
        SendReceipt sendReceipt = null;
        try {
            //发送消息，需要关注发送结果，并捕获失败等异常。
            sendReceipt = getProducer(topic).sendAsync(getMessage(topic, key, tag, msg)).get(3000, TimeUnit.MILLISECONDS);
            System.out.println(sendReceipt.getMessageId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("sendMsgSync end:sendReceipt={}", sendReceipt);
        return sendReceipt;
    }
}
