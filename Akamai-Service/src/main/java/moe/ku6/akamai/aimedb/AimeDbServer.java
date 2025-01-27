package moe.ku6.akamai.aimedb;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.aimedb.handler.AimeDbPacketHandler;
import moe.ku6.akamai.aimedb.packet.AimeDbPacket;
import moe.ku6.akamai.aimedb.packet.AimeDbPacketType;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class AimeDbServer extends ChannelInboundHandlerAdapter {
    @Getter
    private static AimeDbServer instance;

    @Value("${akamai.sega.aimdb.port}")
    private int port;
    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    @Getter
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Map<AimeDbPacketType, AimeDbPacketHandler> handlers = new HashMap<>();

    @PostConstruct
    private void Init() {
        instance = this;

        // reflections search for handlers
        new Reflections("moe.ku6.akamai.aimedb.handler.impl")
                .getSubTypesOf(AimeDbPacketHandler.class)
                .forEach(handlerClass -> {
                    try {
                        AimeDbPacketHandler handler = handlerClass.getConstructor().newInstance();
                        var type = handler.GetPacketType();
                        handlers.put(type, handler);

                    } catch (Exception e) {
                        log.error("Failed to load handler {}", handlerClass.getName(), e);
                    }
                });

        log.info("Registered {} handlers", handlers.size());

        StartServer();
    }

    private void StartServer() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast("encoder", new AimeDbEncoder())
                                .addLast("decoder", new AimeDbDecoder())
                                .addLast("handler", new AimeDbConnection(ch));
                    }
                });

        bootstrap.bind(port)
                .addListener(future -> {
                    if (future.isSuccess()) {
                        log.info("AimeDbServer started on port {}", port);
                    } else {
                        log.error("Failed to start AimeDbServer on port {}", port);
                    }
                });



        Runtime.getRuntime().addShutdownHook(new Thread(this::Shutdown));
    }

    private void Shutdown() {
        log.info("Shutting down AimeDbServer");

        bossGroup.shutdownNow();
        workerGroup.shutdownNow();
        executorService.shutdown();
        log.info("AimeDbServer shutdown complete");
    }

    public AimeDbPacketHandler GetHandler(AimeDbPacketType type) {
        return handlers.get(type);
    }

}
