package org.chatapp.producer;

import java.net.InetAddress;
import java.net.UnknownHostException;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class RandomPartitioner implements Partitioner {
    public RandomPartitioner(VerifiableProperties props) {
    }

    public RandomPartitioner() {
    }

    private int i;

    @Override
    public int partition(Object key, int a_numPartitions) {
        if (i == a_numPartitions)
            i = 0;
        // System.out.println("partition->" + i);
        return i++;
        // int partition = new Random().nextInt(100) % a_numPartitions;
        // System.out.println(partition);
    }

    public static void main(String[] args) throws UnknownHostException {
        byte[] ipAddr = new byte[] { 10, 106, 6, 39 };
        InetAddress addr = InetAddress.getByAddress(ipAddr);
        System.out.println(addr.getCanonicalHostName());
        String hostnameCanonical = addr.getCanonicalHostName();
        RandomPartitioner randomPartitioner = new RandomPartitioner();
        for (int i = 0; i < 100; i++)
            ;
        // System.out.println(randomPartitioner.partition("", 5));
    }
}