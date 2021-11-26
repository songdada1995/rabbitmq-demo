package com.example.consumer.constants;

public class RabbitMqConstant {

    /* ========== 死信队列 ========== */
    /* 队列名 */
    public static final String DEAD_LETTER_QUEUE_2 = "dlx.queue2";
    /* 死信交换机 */
    public static final String DEAD_LETTER_EXCHANGE = "dlx-exchange";
    /* 死信路由键 */
    public static final String DEAD_LETTER_QUEUE_2_ROUTING_KEY = DEAD_LETTER_QUEUE_2 + ".routingKey";

    /* ========== Direct队列 ========== */
    /* 队列名 */
    public static final String DIRECT_QUEUE_NAME_2 = "direct.queue-2";
    /* 交换机名 */
    public static final String DIRECT_EXCHANGE_NAME = "direct-exchange";
    /* 路由键名 */
    public static final String DIRECT_QUEUE_2_ROUTING_KEY_A = DIRECT_QUEUE_NAME_2 + ".pink";
    public static final String DIRECT_QUEUE_2_ROUTING_KEY_B = DIRECT_QUEUE_NAME_2 + ".green";

    /* ========== Topic队列 ========== */
    /* 队列名 */
    public static final String TOPIC_QUEUE_NAME_2 = "topic.queue-2";
    /* 交换机名 */
    public static final String TOPIC_EXCHANGE_NAME_2 = "topic-exchange2";
    /* 路由键名 */
    public static final String TOPIC_QUEUE_2_ROUTING_KEY = TOPIC_QUEUE_NAME_2 + ".#";
}
