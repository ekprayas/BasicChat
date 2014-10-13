package org.chatapp.consumer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.chatapp.common.SentimentScore;
import org.chatapp.common.SentimentStore;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

public class SentimentConsumer implements Runnable {
    private final KafkaStream m_stream;
    private final int m_threadNumber;

    public SentimentConsumer(KafkaStream a_stream, int a_threadNumber) {
        m_threadNumber = a_threadNumber;
        m_stream = a_stream;
    }

    @Override
    public void run() {
        ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
        while (it.hasNext()) {

            ByteArrayInputStream arrayInputStream =
                new ByteArrayInputStream(it.next().message());
            ObjectInputStream objectInputStream = null;
            Object obj = null;
            try {
                objectInputStream = new ObjectInputStream(arrayInputStream);
                obj = objectInputStream.readObject();
            } catch (IOException e) {
                System.out.println("Error while reading object");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            SentimentScore score = (SentimentScore) obj;
            System.out.println("Score recieved: " + score.toString());
            SentimentStore.getInstance().updateSentimentStoreMap(score);
            SentimentStore.getInstance().updatePairSentimentStoreMap(score);

        }
        System.out.println("Shutting down Thread: " + m_threadNumber);
    }
}
