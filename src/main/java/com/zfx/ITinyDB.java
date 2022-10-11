package com.zfx;


import com.zfx.redis.RedisToken;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;

public interface ITinyDB {

    void channel(SocketChannel channel);

    void connect(ChannelHandlerContext ctx);

    void disconnected(ChannelHandlerContext ctx);

    void receive(ChannelHandlerContext ctx, RedisToken<?> message);

}