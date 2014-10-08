package org.chatapp.producer;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;

import org.chatapp.common.Message;

public class MessageEventHandler implements  Runnable {

    private Producer<Integer, Object> producer;
    private String topic;
    private ConcurrentLinkedQueue<Message> eventQ;
    private boolean initialized;

    public MessageEventHandler() {
    }

    public void init() {
        if (initialized)
            return;
        this.eventQ = new ConcurrentLinkedQueue<Message>();
        new Thread(this, MessageEventHandler.class.getName()).start();
        initialized = true;

    }

    public void initProducer() {
//        Properties props = new Properties();
//        props.put("zk.connect", getNodeConfigManager()
//            .getProperty("zk_connect"));
//        props.put("metadata.broker.list",
//            getNodeConfigManager().getProperty("metadata_broker_list"));
//        props.put("serializer.class",
//            getNodeConfigManager().getProperty("serializer_class"));
//        props.put("partitioner.class",
//            getNodeConfigManager().getProperty("partitioner_class"));
//        ProducerConfig config = new ProducerConfig(props);
//        getLogger().info("Initializing kafka with " + props);
//        producer = new Producer<Integer, Object>(config);
//        this.topic = getNodeConfigManager().getProperty("kafka_topic");

    }

      
    public void handleEvents(Message event) {
        eventQ.offer(event);
    }

    public Message getEventFromQueue() {
        return eventQ.poll();
    }

    public void handleFailedEvents(Message event) {
        //getBean(EventManager.class).notifyFailedEvent(event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        while (true) {
            if (!eventQ.isEmpty()) {
            	Message event = getEventFromQueue();
                if (!send(event, topic))
                    handleFailedEvents(event);
            } else {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public boolean send(List<Message> events, String topic) {
        try {
            if (topic == null) {
                getProducer();
                topic = topic == null ? this.topic : topic;
            }
            getProducer().send(
                new KeyedMessage<Integer, Object>(topic, 0, events));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean send(Message event, String topic) {
        try {
            if (topic == null) {
                getProducer();
                topic = topic == null ? this.topic : topic;
            }
            getProducer().send(
                new KeyedMessage<Integer, Object>(topic, 0, event));
            return true;
        } catch (Exception e) {
            
            return false;
        }
    }

   
    /**
     * @return the producer
     */
    public Producer<Integer, Object> getProducer() {
        if (producer == null)
            initProducer();
        return producer;
    }

    /**
     * @param producer
     *            the producer to set
     */
    public void setProducer(Producer<Integer, Object> producer) {
        this.producer = producer;
    }

    /**
     * @return the topic
     */
    public String getTopic() {
        return topic;
    }

    /**
     * @param topic
     *            the topic to set
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void destroy() {
        initialized = false;
        getProducer().close();
    }

}