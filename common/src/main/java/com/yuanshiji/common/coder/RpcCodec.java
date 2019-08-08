package com.yuanshiji.common.coder;

import com.yuanshiji.common.serialization.Serialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

/**
 * @author yuanshijia
 * @date 2019-08-08
 * @description
 * 自定义netty编码、解码器
 *
 */
public class RpcCodec extends ByteToMessageCodec {

    private Class<?> clazz;
    private Serialization serialization;

    public RpcCodec(Class<?> clazz, Serialization serialization) {
        this.clazz = clazz;
        this.serialization = serialization;
    }


    /**
     * 编码
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (clazz != null) {
            byte[] data = serialization.serialize(msg);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }

    /**
     * 解码　
     * @param channelHandlerContext
     * @param in
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List list) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }

        int dataLength = in.readInt();
        in.markReaderIndex();
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }

        byte[] data = new byte[dataLength];
        in.readBytes(data);

        Object obj = serialization.doSerialize(data, clazz);
        list.add(obj);
    }
}
