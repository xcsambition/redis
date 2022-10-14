package com.zfx;

import com.zfx.command.*;
import com.zfx.command.impl.*;
import com.zfx.data.Database;
import com.zfx.data.DatabaseValue;
import com.zfx.redis.RedisToken;
import com.zfx.redis.RedisToken.ArrayRedisToken;
import com.zfx.redis.RedisToken.UnknownRedisToken;
import com.zfx.redis.RedisTokenType;
import com.zfx.redis.RequestDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author zfx
 */
@Slf4j
public class TinyDB implements ITinyDB {

    private static final int BUFFER_SIZE = 1024 * 1024;
    // Max message size
    private static final int MAX_FRAME_SIZE = BUFFER_SIZE * 100;


    private static final String DEFAULT_HOST = "localhost";

    private static final int DEFAULT_PORT = 8031;

    private final int port;

    private final String host;

    private ChannelFuture future;

    private final Database db = new Database(new HashMap<String, DatabaseValue>());

    private final Map<String, ICommand> commands = new HashMap<>();

    private final Map<String, ChannelHandlerContext> channels = new HashMap<>();

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private ServerBootstrap bootstrap;

    private TinyDBInitializerHandler acceptHandler;

    private TinyDBConnectionHandler connectionHandler;

    public TinyDB() {
        this.host = DEFAULT_HOST;
        this.port = DEFAULT_PORT;
    }

    private void init() {
        // connection
        commands.put("ping", new CommandWrapper(new PingCommand()));
        commands.put("echo", new CommandWrapper(new EchoCommand()));

        // server
        commands.put("flushdb", new CommandWrapper(new FlushDBCommand()));
        commands.put("time", new CommandWrapper(new TimeCommand()));

        // strings
        commands.put("get", new CommandWrapper(new GetCommand()));
        commands.put("mget", new CommandWrapper(new MultiGetCommand()));
        commands.put("set", new CommandWrapper(new SetCommand()));
        commands.put("incr", new CommandWrapper(new IncrementCommand()));
        commands.put("incrby", new CommandWrapper(new IncrementByCommand()));
        commands.put("decr", new CommandWrapper(new DecrementCommand()));
        commands.put("decrby", new CommandWrapper(new DecrementByCommand()));

        // keys
        commands.put("del", new CommandWrapper(new DeleteCommand()));
        commands.put("exists", new CommandWrapper(new ExistsCommand()));
        commands.put("keys", new CommandWrapper(new KeysAllCommand()));

        // hash
        commands.put("hset", new CommandWrapper(new HashSetCommand()));
        commands.put("hget", new CommandWrapper(new HashGetCommand()));
        commands.put("hgetall", new CommandWrapper(new HashGetAllCommand()));

    }

    private void start() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
        acceptHandler = new TinyDBInitializerHandler(this);
        connectionHandler = new TinyDBConnectionHandler(this);

        bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(acceptHandler)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_RCVBUF, BUFFER_SIZE)
                .option(ChannelOption.SO_SNDBUF, BUFFER_SIZE)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        // Bind and start to accept incoming connections.
        future = bootstrap.bind(host, port);
        try {
            future.sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("adapter started: {}:{}", host, port);


    }

    public void stop() {
        try {
            if (future != null) {
                future.channel().close();
            }
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

        channels.clear();

        log.debug("adapter stopped");
    }


    @Override
    public void channel(SocketChannel channel) {
        log.debug("new channel:" + sourceKey(channel));

        channel.pipeline().addLast("StringEncoder", new StringEncoder(Charset.forName("UTF-8")));
        channel.pipeline().addLast("linDelimiter", new RequestDecoder(MAX_FRAME_SIZE));
        channel.pipeline().addLast(connectionHandler);
    }


    @Override
    public void connect(ChannelHandlerContext ctx) {
        String sourceKey = sourceKey(ctx.channel());

        log.debug("client connected : {}", sourceKey);

        channels.put(sourceKey, ctx);

    }

    @Override
    public void disconnected(ChannelHandlerContext ctx) {
        String sourceKey = sourceKey(ctx.channel());

//        logger.debug("client disconnected : {}",sourceKey);

        channels.remove(sourceKey);
    }

    @Override
    public void receive(ChannelHandlerContext ctx, RedisToken<?> message) {

        String sourceKey = sourceKey(ctx.channel());
        log.debug("message received: {}", sourceKey);
        String s = processCommand(parseMessage(message));
        ctx.writeAndFlush(s);

    }

    private IRequest parseMessage(RedisToken<?> message) {
        Request request = new Request();
        if (message.getType() == RedisTokenType.ARRAY) {
            parseArray((ArrayRedisToken) message, request);
        } else if (message.getType() == RedisTokenType.UNKNOWN) {
            parseLine(message, request);
        }
        return request;
    }

    private static void parseLine(RedisToken<?> message, Request request) {
        UnknownRedisToken unknownToken = (UnknownRedisToken) message;
        String command = unknownToken.getValue();
        String[] params = command.split(" ");
        String[] array = new String[params.length - 1];
        System.arraycopy(params, 1, array, 0, array.length);
        request.setParams(Arrays.asList(array));
    }

    private void parseArray(ArrayRedisToken message, Request request) {
        ArrayRedisToken arrayToken = message;
        LinkedList<String> params = new LinkedList<>();
        for (RedisToken<?> token : arrayToken.getValue()) {
            params.add(token.getValue().toString());
        }
        request.setCommand(params.get(0));
        request.setParams(params.subList(1, params.size()));
    }

    private String processCommand(IRequest request) {
        log.debug("received command: " + request);

        IResponse response = new Response();
        ICommand command = commands.get(request.getCommand().toLowerCase());
        if (command != null) {
            command.execute(db, request, response);
        } else {
            response.addError("ERR unknown command `" + request.getCommand() + "`");
        }
        return response.toString();
    }


    private String sourceKey(Channel channel) {
        InetSocketAddress remoteAddress = (InetSocketAddress) channel.remoteAddress();
        return remoteAddress.getHostName() + ":" + remoteAddress.getPort();
    }


    public static void main(String[] args) {
        TinyDB db = new TinyDB();
        db.init();
        db.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> db.stop()));

    }


}
