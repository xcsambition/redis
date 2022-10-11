import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

@Slf4j
public class Client {
    private static String host = "127.0.0.1";
    private static int port = 8031;

//    private static Logger logger = Logger.getLogger(Client.class);
    public static void main(String[] args) throws Exception{

        Channel channel = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                                ch.pipeline()
                                        .addLast(new StringEncoder(Charset.defaultCharset()))
                                        .addLast(new ChannelInboundHandlerAdapter(){
                                            @Override
                                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                                ByteBuf buf = (ByteBuf) msg;
                                                log.debug(buf.toString(Charset.defaultCharset()));
                                            }
                                        });
                    }
                }).connect(new InetSocketAddress(host,port))
                .sync()
                .channel();
        channel.writeAndFlush("aaa");
        log.debug(channel.toString());
        log.debug("  ");



    }
}
