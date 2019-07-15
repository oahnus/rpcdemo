# RPC Demo
RPC服务器Demo,并托管于Spring 

## Modules
- rpc-api api公共包
- rpc-client 客户端 内含Socket与NIO Socket两种实现
- rpc-server 服务端 内含Socket与NIO Socket两种实现

## Config
Server端在SpringConfig中设置RPC服务器运行方式
Client端在RpcSender中设置发送普通Socket请求还是NIO请求

## Run
```
// Server端
ServerApp.main()

// Client端
ClientApp.main()
```