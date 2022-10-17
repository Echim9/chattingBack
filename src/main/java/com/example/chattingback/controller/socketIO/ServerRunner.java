package com.example.chattingback.controller.socketIO;

import com.corundumstudio.socketio.SocketIOServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Echim9
 * @date 2022/10/11 00:41
 *
 * 统一配置应用出事化处
 */
@Component
public class ServerRunner  implements ApplicationListener<ContextRefreshedEvent>{

    private Logger logger = LoggerFactory.getLogger(ServerRunner.class);

    @Resource
    private SocketIOHandler socketIOHandler;
    @Resource
    private SocketIOServer socketIOServer;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null){
            socketIOServer.addListeners(socketIOHandler);
            socketIOServer.start();
            logger.info("socket-io启动成功");
        }
    }





    @Autowired
    public ServerRunner(SocketIOServer server) {
        this.socketIOServer = server;
    }


}

