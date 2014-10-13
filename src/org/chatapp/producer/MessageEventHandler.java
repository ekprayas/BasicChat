package org.chatapp.producer;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.chatapp.common.Message;

import scala.reflect.generic.Trees.This;

public class MessageEventHandler implements  Runnable {

	
    private static MessageEventHandler eventHandler = new MessageEventHandler();
	private Producer<String, String> producer;
    private String topic;
    private ConcurrentLinkedQueue<String> eventQ;
    private boolean initialized;

    public static MessageEventHandler getInstance()
    {
    	return eventHandler;
    }
    public MessageEventHandler() {
    	init();
    }

    public void init() {
        if (initialized)
            return;
        this.eventQ = new ConcurrentLinkedQueue<String>();
        new Thread(this, MessageEventHandler.class.getName()).start();
        initialized = true;

    }

    public void initProducer() {
        Properties props = new Properties();
        props.put("zk.connect", "10.14.122.205:2181");
        props.put("metadata.broker.list",
        		"10.14.122.205:9092");
        props.put("serializer.class",
        		"kafka.serializer.StringEncoder");
        props.put("partitioner.class", "org.chatapp.producer.RandomPartitioner");
        System.out.println("Broker:" + props.get("metadata.broker.list"));
        ProducerConfig config = new ProducerConfig(props);
        System.out.println("Broker:" + props.get("metadata.broker.list"));
        System.out.println("Initializing kafka with " + props);
        producer = new Producer<String, String>(config);
        this.topic = "messages";

    }

      
    public void handleEvents(String chatString) {
        eventQ.offer(chatString);
    }

    public String getEventFromQueue() {
        return eventQ.poll();
    }

    public void handleFailedEvents(String event) {
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
            	String event = getEventFromQueue();
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

  /*  public boolean send(List<String> events, String topic) {
        try {
            if (topic == null) {
                getProducer();
                topic = topic == null ? this.topic : topic;
            }
            getProducer().send(
                new KeyedMessage<String, String>(topic, events));
            return true;
        } catch (Exception e) {
            return false;
        }
    }*/

    public boolean send(String message, String topic) {
        try {
            getProducer().send(
                new KeyedMessage<String, String>(topic, "0", message));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

   
    /**
     * @return the producer
     */
    public Producer<String, String> getProducer() {
        if (producer == null)
            initProducer();
        return producer;
    }

    /**
     * @param producer
     *            the producer to set
     */
    public void setProducer(Producer<String, String> producer) {
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