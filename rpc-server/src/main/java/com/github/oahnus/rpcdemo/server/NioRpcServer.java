package com.github.oahnus.rpcdemo.server;

import com.github.oahnus.rpcdemo.api.RpcRequest;
import com.github.oahnus.rpcdemo.api.RpcResponse;
import com.github.oahnus.rpcdemo.api.RpcVersion;
import com.github.oahnus.rpcdemo.api.SerializeUtil;
import com.github.oahnus.rpcdemo.server.annotation.RpcService;
import com.github.oahnus.rpcdemo.server.process.NioProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by oahnus on 2019/7/12
 * 14:09.
 */
@Component
public class NioRpcServer implements ApplicationContextAware, InitializingBean {
    private static final int PORT = 7891;
    private static ByteBuffer buffer = ByteBuffer.allocate(2048);

    // Rpc服务容器
    private Map<String, Object> handlerMap = new HashMap<>();

    public void afterPropertiesSet() {
        Selector selector;
        try {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ServerSocket ss = ssc.socket();
            ss.bind(new InetSocketAddress(PORT));
            ssc.configureBlocking(false);

            selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("NIO RPC Server Start Listening On Port:" + PORT);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("RPC Server Start Failure");
        }

        while (true) {
            try {
                int n = selector.select(3000);
                if (n == 0) {
                    continue;
                }

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();

                    if (selectionKey.isAcceptable()) {
                        System.out.println("accept");
                        ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel = channel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {
                        System.out.println("Readable");
                        // Decode RPC Request
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        byte[] bytes = SerializeUtil.readDataFromSocketChannel(socketChannel);
                        RpcRequest request = SerializeUtil.deserialize(bytes, RpcRequest.class);

                        /// RUN Method
                        NioProcessor processor = new NioProcessor(handlerMap);
                        RpcResponse response = processor.handle(request);

                        // Encode RPC Method Return Value
                        bytes = SerializeUtil.serialize(response, RpcResponse.class);
                        selectionKey.attach(ByteBuffer.wrap(bytes));

                        selectionKey.interestOps(SelectionKey.OP_WRITE);
                        selectionKey.selector().wakeup();
                    } else if (selectionKey.isWritable()) {
                        System.out.println("Writable");
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        // Get Response Data From SelectionKey
                        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();

                        if(buffer == null || !buffer.hasRemaining()) {
                            return;
                        }

                        // Send
                        socketChannel.write(buffer);

                        if(!buffer.hasRemaining()){
                            selectionKey.interestOps(SelectionKey.OP_READ);
                            buffer.clear();
                        }
                        selectionKey.cancel();
                        socketChannel.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 获取注解RpcService的Bean
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (!beans.isEmpty()) {
            for (Object bean : beans.values()) {
                // 将Bean注册到容器中
                RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
                Class<?> rpcServiceClass = rpcService.value();
                RpcVersion rpcVersion = rpcService.version();

                String clazzName = rpcServiceClass.getName();
                String key = clazzName + "-" + rpcVersion.getVersion();
                handlerMap.put(key, bean);
            }
        }
    }
}
