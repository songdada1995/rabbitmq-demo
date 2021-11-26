package com.example.consumer.constants;

public class RabbitMqConstant {

    /* ========== 死信队列 ========== */
    /* 队列 */
    public static final String DEAD_LETTER_QUEUE_1 = "dlx.queue1";
    /* 交换机 */
    public static final String DEAD_LETTER_EXCHANGE = "dlx-exchange";
    /* 路由键 */
    public static final String DEAD_LETTER_QUEUE_1_ROUTING_KEY = DEAD_LETTER_QUEUE_1 + ".routingKey";

    /* ========== Direct队列 ========== */
    /* 队列名 */
    public static final String DIRECT_QUEUE_NAME_1 = "direct.queue-1";
    /* 交换机名 */
    public static final String DIRECT_EXCHANGE_NAME = "direct-exchange";
    /* 路由键名 */
    public static final String DIRECT_QUEUE_1_ROUTING_KEY_A = DIRECT_QUEUE_NAME_1 + ".blue";
    public static final String DIRECT_QUEUE_1_ROUTING_KEY_B = DIRECT_QUEUE_NAME_1 + ".pink";

    /* ========== Topic队列 ========== */
    /* 队列名 */
    public static final String TOPIC_QUEUE_NAME_1 = "topic.queue-1";
    /* 交换机名 */
    public static final String TOPIC_EXCHANGE_NAME_1 = "topic-exchange1";
    /* 路由键名 */
    public static final String TOPIC_QUEUE_1_ROUTING_KEY = TOPIC_QUEUE_NAME_1 + ".routingKey-1";
}
