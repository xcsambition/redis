package com.zfx;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import com.zfx.redis.RedisToken;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
@Sharable
public class TinyDBConnectionHandler extends ChannelInboundHandlerAdapter {

//    private static final Logger logger = LoggerFactory.getLogger(TinyDBConnectionHandler.class);



    private final ITinyDB impl;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        impl.connect(ctx);
    }

    public TinyDBConnectionHandler(ITinyDB impl) {
        this.impl = impl;
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            impl.receive(ctx, (RedisToken<?>) msg);
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        logger.debug("channel inactive");
        impl.disconnected(ctx);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        logger.error("uncaught exception", cause);
        impl.disconnected(ctx);
        ctx.close();
    }
}
