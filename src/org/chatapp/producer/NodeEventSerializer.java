package org.chatapp.producer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;

@Deprecated
public class NodeEventSerializer implements Encoder<Object> {

    public NodeEventSerializer() {
    }

    public NodeEventSerializer(VerifiableProperties properties) {
    }

    @Override
    public byte[] toBytes(Object events) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream bos = null;
        OutputStream os = null;
        // GZIPOutputStream gzipos = null;
        try {
            os = bos = new ByteArrayOutputStream(2048);
            // os = gzipos = new GZIPOutputStream(os);
            os = oos = new ObjectOutputStream(os);
            oos.writeObject(events);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.flush();
                os.close();
                // closeQuietly(pipeIO.getIn());
            } catch (Exception e) {
                e.printStackTrace();
                // ignore
            }
        }
        return bos.toByteArray();
    }
}
