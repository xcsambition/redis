import com.zfx.TinyDB;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private static String host = "127.0.0.1";
    private static int port = 8031;

    private static Logger logger = Logger.getLogger(Client.class);
    public static void main(String[] args) throws Exception{



        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(workGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                                ch.pipeline()
                                        .addLast(new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        ByteBuf buf = (ByteBuf) msg;
                                        System.out.print(buf.toString(StandardCharsets.UTF_8));
                                    }
                                });
                    }
                });
        ChannelFuture connect = bootstrap.connect(host, port);
        Channel channel = connect.channel();
        connect.addListener(future -> {
            if (future.isSuccess()) {
                logger.info("connect success");
            }else {
                logger.info("please connect again\n host:"+host + ",port:"+port);
            }
        });
        connect.sync();
        try (Scanner sc = new Scanner(System.in)){
            while (true) {
                String text = sc.nextLine();
                if (text.isEmpty()) continue;
                channel.writeAndFlush(Unpooled.wrappedBuffer((text+"\n").getBytes()));
            }
        }



    }
}
