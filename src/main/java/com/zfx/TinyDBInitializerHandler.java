package com.zfx;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class TinyDBInitializerHandler extends ChannelInitializer<SocketChannel> {

    private ITinyDB impl;

    public TinyDBInitializerHandler(ITinyDB impl) {
        this.impl = impl;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        impl.channel(ch);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        impl.disconnected(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
    }
}
