

# 一、RocketMQ实践

## 1.1 安装启动RocketMQ

### 1.1.1 安装RocketMQ

1. ### 下载源码

   ```
   git clone https://github.com/apache/rocketmq.git
   ```

2. ### 编译源码

   ```
   mvn -Prelease-all -DskipTests clean install -U
   ```

   编译结果：

   ![image-20220925101112968](/Users/huayingcao/Library/Application Support/typora-user-images/image-20220925101112968.png)

   编译之后会在源码的根目录下生成distribution的目录，该目录中会生成如下文件:

   ![image-20220925101748671](/Users/huayingcao/Library/Application Support/typora-user-images/image-20220925101748671.png)


### 1.1.2 启动nameserver

完成编译之后，在target/rocketmq-5.0.0-SNAPSHOT/rocketmq-5.0.0-SNAPSHOT/bin 目录下是可执行的二进制文件：

![image-20220925102334083](/Users/huayingcao/Library/Application Support/typora-user-images/image-20220925102334083.png)启动NameServer

```bash
### 进入 target/rocketmq-5.0.0-SNAPSHOT/rocketmq-5.0.0-SNAPSHOT/bin 目录下，执行命令：
nohup sh ./bin/mqnamesrv &
### 查看启动结果
```

### 1.1.3 启动Proxy

1.启动

```bash
nohup sh bin/mqproxy -n localhost:9876 &
```

2.查看启动结果

```
cat nohup.log
```

![image-20220925151957561](/Users/huayingcao/Library/Application Support/typora-user-images/image-20220925151957561.png)

3.关闭



### 1.1.4 启动broker

1.设置nameserver地址

```bash
export NAMESRV_ADDR=localhost:9876
```

2.启动

```bash
nohup sh bin/mqbroker -n localhost:9876 & autoCreateTopicEnable=true
```

3.查看启动结果

![image-20220925182051045](/Users/huayingcao/Library/Application Support/typora-user-images/image-20220925182051045.png)

## 1.2 安装启动RocketMQ Console

Console在最新的版本中已经更名为Dashboard，下载地址为：https://github.com/apache/rocketmq-dashboard

![image-20220925145023021](/Users/huayingcao/Library/Application Support/typora-user-images/image-20220925145023021.png)

### 1.下载源码

```bash
git clone https://github.com/apache/rocketmq-dashboard.git
```

### 2.编译

```bash
mvn clean package -Dmaven.test.skip=true
```

### 3.运行

```bash
java -jar target/rocketmq-dashboard-1.0.1-SNAPSHOT.jar
```

启动结果如下：

![image-20220925145627878](/Users/huayingcao/Library/Application Support/typora-user-images/image-20220925145627878.png)

### 4.访问

访问8080端口：

```bash
http://localhost:8080/#/
```

页面如下，可以看到刚创建的集群：

![image-20220925182450298](/Users/huayingcao/Library/Application Support/typora-user-images/image-20220925182450298.png)

## 1.3 发送消息

1.发送消息

```bash
sh bin/tools.sh org.apache.rocketmq.example.quickstart.Producer
```

2.查看发送结果

在dashboard中查看消息    ⚠️注意：需要选择对应的topic和时间段

![image-20220925182855319](/Users/huayingcao/Library/Application Support/typora-user-images/image-20220925182855319.png)

3.查看消费详情

查看未消费之前查看消费情况，方便对比！

![image-20220925183114158](/Users/huayingcao/Library/Application Support/typora-user-images/image-20220925183114158.png)

## 1.4 消费消息

1.消费消息

```bash
sh bin/tools.sh org.apache.rocketmq.example.quickstart.Consumer
```

2.查看消费结果

![image-20220925183921680](/Users/huayingcao/Library/Application Support/typora-user-images/image-20220925183921680.png)

3.查看消费详情

消费之后，在dashboard中查看消费详情

![image-20220925183734996](/Users/huayingcao/Library/Application Support/typora-user-images/image-20220925183734996.png)

## 1.5 安装启动问题

### 1.5.1 启动nameserver报错

报错信息：端口被占用

排查问题：

```bash
### mac查看端口启用情况
nc -zv -w 2 -u 192.168.1.9 端口号（9876）
### 结果：Connection to 192.168.1.9 port 9876 [udp/sd] succeeded!

### mac解决端口占用情况：
### 1.查看端口占用
lsof -i tcp:9876

### 2.杀掉占用端口的进程：
kill -9 PID
```

解决方式：修改启动脚本（bin/runserver.sh）

![image-20220925131443819](/Users/huayingcao/Library/Application Support/typora-user-images/image-20220925131443819.png)

### 1.5.2 发送消息报错

报错信息：

启动broker的时候报错如下：

![image-20220925150828087](/Users/huayingcao/Library/Application Support/typora-user-images/image-20220925150828087.png)

问题排查：没有这个topic

问题解决：需要自动设置自动创建Topic的参数

```bash
autoCreateTopicEnable=true
```



# 二、消息重复

rocketmq只能保证消息不丢失，不能保证消息不重复！



# 三、消息对堆积

消息堆积：消费者处理速度跟不上生产者发送消息的速度

消费延迟：消息堆积导致的消费延迟

> CPU密集型代码：for循环、递归
>
> IO密集型代码：tcp、http、rpc调用

> 单节点线程数计算：
>
> C*(T1+T2)/T1=C*T1/T1+C*T2/T1=C+C*T2/T1
>
> C：CPU核数
>
> T1：CPU内部计算耗时
>
> T2：外部IO操作耗时

# 四、消息的清理

![image-20220925084717054](/Users/huayingcao/Library/Application Support/typora-user-images/image-20220925084717054.png)

