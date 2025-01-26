package moe.ku6.akamai.aimedb;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AimeDbServer extends ChannelInboundHandlerAdapter {
    @Value("${akamai.sega.aimdb.port}")
    private int port;
    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    @PostConstruct
    private void Init() {
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
        log.info("AimeDbServer shutdown complete");
    }


}
