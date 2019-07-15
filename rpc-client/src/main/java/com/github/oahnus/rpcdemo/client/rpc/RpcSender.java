package com.github.oahnus.rpcdemo.client.rpc;

import com.github.oahnus.rpcdemo.api.RpcRequest;
import com.github.oahnus.rpcdemo.api.RpcResponse;
import com.github.oahnus.rpcdemo.api.SerializeUtil;
import lombok.Cleanup;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by oahnus on 2019/7/12
 * 14:42.
 */
public class RpcSender {
    public static Object sendRpcRequest(String ip, int port, RpcRequest rpcRequest) {
        OutputStream out = null;
        try {
            Socket socket = new Socket(ip, port);
            byte[] reqBytes = SerializeUtil.serialize(rpcRequest, RpcRequest.class);

            out = socket.getOutputStream();
            out.write(reqBytes);
            out.flush();
            socket.shutdownOutput();

            byte[] respBytes = SerializeUtil.readInputStream(socket.getInputStream());
            RpcResponse response = SerializeUtil.deserialize(respBytes, RpcResponse.class);

            return response.getResultVal();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static Object sendNioRpcRequest(String ip, int port, RpcRequest rpcRequest) {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(true);
            socketChannel.connect(new InetSocketAddress(ip, port));

            while (!socketChannel.finishConnect()) {
                Thread.sleep(50);
            }

            byte[] reqByes = SerializeUtil.serialize(rpcRequest, RpcRequest.class);
            System.out.println("sendRequest:");
            socketChannel.write(ByteBuffer.wrap(reqByes));
            socketChannel.shutdownOutput();

            socketChannel.socket().setSoTimeout(1000); // timeout
            byte[] bytes = SerializeUtil.readDataFromSocketChannel(socketChannel);
            RpcResponse resp = SerializeUtil.deserialize(bytes, RpcResponse.class);

            socketChannel.close();
            return resp.getResultVal();
        } catch (SocketTimeoutException e) {
            throw new RuntimeException("RPC Request Timeout");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
