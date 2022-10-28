package com.zfx.persistence;

import com.github.tonivade.resp.protocol.SafeString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ByteBufferInputStreamTest {

    @Test
    void testStream() {
        ByteBufferInputStream in = new ByteBufferInputStream(SafeString.fromHexString("09486F6C61206D756E646F21").getBytes());

        System.out.println(in.read());
    }
}