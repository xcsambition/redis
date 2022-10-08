package com.zfx;




import com.zfx.command.*;
import com.zfx.command.impl.*;
import com.zfx.data.Database;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;


import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TinyDB implements ITinyDB {

    private static Logger logger = Logger.getLogger(TinyDB.class);
    private static final int BUFFER_SIZE = 1024 * 1024;
    // Max message size
    private static final int MAX_FRAME_SIZE = BUFFER_SIZE * 100;


    private static final String DEFAULT_HOST = "localhost";

    private static final int DEFAULT_PORT = 8031;

    private int port;

    private String host;

    private ChannelFuture future;

    private Database db = new Database();

    private Map<String, ICommand> commands = new HashMap<>();

    private Map<String, ChannelHandlerContext> channels = new HashMap<>();

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
        commands.put("decr",new CommandWrapper(new DecrementCommand(),1));
        commands.put("del",new CommandWrapper(new DelCommand(),1));
        commands.put("echo",new CommandWrapper(new EchoCommand(),1));
        commands.put("exists",new CommandWrapper(new ExistsCommand(),1));
        commands.put("get", new CommandWrapper(new GetCommand(), 1));
        commands.put("incr",new CommandWrapper(new IncrementCommand(),1));
        commands.put("mget",new CommandWrapper(new MultiGetCommand(),1));
        commands.put("ping", new PingCommand());
        commands.put("set", new CommandWrapper(new SetCommand(),2));
    }

    private void start() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
        acceptHandler = new TinyDBInitializerHandler(this);
        connectionHandler = new TinyDBConnectionHandler(this);

        try {
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


        } catch (RuntimeException e) {
                logger.error(e.getMessage());
                throw new TinyDBException(e);
        }
        logger.info("adapter started!\n"+host + ",port:"+port);
    }

    public void stop() {
        try {
            if (future !=null) {
                future.channel().close();
            }
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

//        logger.debug("adapter stopped");
    }


    @Override
    public void channel(SocketChannel channel) {
        channel.pipeline().addLast("StringEncoder",new StringEncoder(CharsetUtil.UTF_8));
        channel.pipeline().addLast("linDelimiter",
                new DelimiterBasedFrameDecoder(MAX_FRAME_SIZE, true, Delimiters.lineDelimiter()));
        channel.pipeline().addLast("stringDecoder",new StringDecoder(CharsetUtil.UTF_8));
        channel.pipeline().addLast(connectionHandler);
    }


    @Override
    public void connect(ChannelHandlerContext ctx) {
        String sourceKey = sourceKey(ctx.channel());

//        logger.debug("client connected : {}",sourceKey);

        channels.put(sourceKey, ctx);

    }

    @Override
    public void disconnected(ChannelHandlerContext ctx) {
        String sourceKey = sourceKey(ctx.channel());

//        logger.debug("client disconnected : {}",sourceKey);

        channels.remove(sourceKey);
    }

    @Override
    public void receive(ChannelHandlerContext ctx, String message) {

        String sourceKey = sourceKey(ctx.channel());
//        logger.debug("message received: {}",sourceKey);

        ctx.writeAndFlush(processCommand(parse(message)));

    }

    private IRequest parse(String message) {
        Request request = new Request();
        String[] split = message.split(" ");
        request.setCommand(split[0]);
        String[] params = new String[split.length - 1];
        System.arraycopy(split, 1, params, 0, params.length);
        request.setParams(Arrays.asList(params));
        request.setParams(Arrays.asList(params));
        return request;
    }

    private String processCommand(IRequest request) {
        String cmd = request.getCommand();

//        LOGGER.log(Level.INFO, "command:{0}", cmd);

        IResponse response = new Response();
        ICommand command = commands.get(cmd);
        if (command != null) {
            command.execute(db, request, response);
        } else {
            response.addError("ERR unknown command '" + cmd + "'");
        }
        return response.toString();

    }



    private String sourceKey(Channel channel) {
        InetSocketAddress remoteAddress = (InetSocketAddress) channel.remoteAddress();
        return remoteAddress.getHostName() + ":" + remoteAddress.getPort();
    }










    public static void main(String[] args) {
        TinyDB db = new TinyDB();
//        logger.debug("开始了");
//        logger.error("error");
        db.init();
        db.start();

    }


}
