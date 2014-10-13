package org.chatapp.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.chatapp.producer.MessageEventHandler;

import com.sun.org.apache.xml.internal.security.Init;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class SentimentConsumerGroup {
    private final ConsumerConnector consumer;
    private final String topic;
    private ExecutorService executor;

    private static SentimentConsumerGroup sentimentConsumerGroup;
    
    public static SentimentConsumerGroup getInstance()
    {
    	if(sentimentConsumerGroup == null)
    	{
    		sentimentConsumerGroup = new SentimentConsumerGroup(
    	    		"10.14.122.205:2181", "chatConsumer", "analysis");
    		init();
    	}
    	return sentimentConsumerGroup;
    }
    
    private static void init() {
    	 sentimentConsumerGroup.run(1);
    }
    
    public SentimentConsumerGroup(String a_zookeeper, String a_groupId,
        String a_topic) {
        consumer =
            kafka.consumer.Consumer
                .createJavaConsumerConnector(createConsumerConfig(a_zookeeper,
                    a_groupId));
        this.topic = a_topic;
    }

    public void shutdown() {
        if (consumer != null)
            consumer.shutdown();
        if (executor != null)
            executor.shutdown();
    }

    public void run(int a_numThreads) {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(a_numThreads));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap =
            consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

        // now launch all the threads
        //
        executor = Executors.newFixedThreadPool(a_numThreads);

        // now create an object to consume the messages
        //
        int threadNumber = 0;
        for (final KafkaStream stream : streams) {
            executor.submit(new SentimentConsumer(stream, threadNumber));
            threadNumber++;
        }
    }

    private static ConsumerConfig createConsumerConfig(String a_zookeeper,
        String a_groupId) {
        Properties props = new Properties();
        props.put("zookeeper.connect", a_zookeeper);
        props.put("group.id", a_groupId);
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "smallest");

        return new ConsumerConfig(props);
    }

    public static void main(String[] args) {
        String zooKeeper = args[0];
        String groupId = args[1];
        String topic = args[2];
        int threads = Integer.parseInt(args[3]);

        SentimentConsumerGroup example =
            new SentimentConsumerGroup(zooKeeper, groupId, topic);
        example.run(threads);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException ie) {

        }
        example.shutdown();
    }
}
