package com.zfx.persistence;

import java.util.zip.Checksum;


/**
 * 循环冗余校验
 */
public class CRC64 implements Checksum {

    private static final int LOOKUPTABLE_SIZE = 256;
    private static final long POLY64REV = 0xC96C5795D7870F42L;
    private static final long[] LOOKUPTABLE = new long[LOOKUPTABLE_SIZE];

    private long crc = -1;

    static {
        for (int b = 0; b < LOOKUPTABLE.length; ++b) {
            long r = b;
            for (int i = 0; i < Long.BYTES; ++i) {
                if ((r & 1) == 1) {
                    r = (r >>> 1) ^ POLY64REV;
                } else {
                    r >>>= 1;
                }
            }
            LOOKUPTABLE[b] = r;
        }
    }

    @Override
    public void update(int b) {
        crc = LOOKUPTABLE[((b & 0xFF) ^ (int) crc) & 0xFF] ^ (crc >>> 8);
    }

    @Override
    public void update(byte[] buf, int off, int len) {
        int end = off + len;

        while (off < end) {
            crc = LOOKUPTABLE[(buf[off++] ^ (int) crc) & 0xFF] ^ (crc >>> 8);
        }
    }

    @Override
    public long getValue() {
        return ~crc;
    }

    @Override
    public void reset() {
        crc = -1;
    }
}

