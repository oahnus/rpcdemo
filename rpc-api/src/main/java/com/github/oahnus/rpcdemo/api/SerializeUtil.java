package com.github.oahnus.rpcdemo.api;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.runtime.RuntimeSchema;
import lombok.Cleanup;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oahnus on 2019/7/12
 * 15:11.
 */
public class SerializeUtil {
    private static final int BUFFER_SIZE = 1024;
    private static Map<Class, RuntimeSchema> schemaMap = new HashMap<>();

    public static <T> byte[] serialize(T obj, Class<T> clazz) {
        RuntimeSchema schema = schemaMap.get(clazz);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(clazz);
            schemaMap.put(clazz, schema);
        }

        byte[] bytes = ProtobufIOUtil.toByteArray(obj, schema,
                LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        return bytes;
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        RuntimeSchema schema = schemaMap.get(clazz);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(clazz);
            schemaMap.put(clazz, schema);
        }
        Object obj = schema.newMessage();
        ProtobufIOUtil.mergeFrom(bytes, obj, schema);
        return (T) obj;
    }

    public static byte[] readDataFromSocketChannel(SocketChannel socketChannel) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024); // Buffer缓存
            byte[] dataBytes = new byte[0];
            while(socketChannel.read(buffer) > 0){
                buffer.flip();
                byte[] dst = new byte[buffer.limit()];
                buffer.get(dst);
                dataBytes = ByteUtil.concat(dataBytes, dst);
                buffer.clear();
            }
            return dataBytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] readInputStream(InputStream in) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int n = in.read(buffer);
            while (n > 0) {
                output.write(buffer, 0, n);
                n = in.read(buffer);
            }
            byte[] bytes = output.toByteArray();
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
