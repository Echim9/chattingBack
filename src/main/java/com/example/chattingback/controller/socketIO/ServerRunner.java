package com.example.chattingback.controller.socketIO;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author Echim9
 * @date 2022/10/11 00:41
 */
@Component
public class ServerRunner  implements ApplicationListener<ContextRefreshedEvent>{

    @Autowired
    private SocketIOHandler socketIOHandler;
    @Autowired
    private SocketIOServer socketIOServer;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null){
            socketIOServer.addListeners(socketIOHandler);
            socketIOServer.start();
            System.out.println("socket.io启动成功");
        }
    }


    @Autowired
    public ServerRunner(SocketIOServer server) {
        this.socketIOServer = server;
    }


}

