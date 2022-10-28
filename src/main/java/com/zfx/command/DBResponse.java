package com.zfx.command;

import com.github.tonivade.resp.protocol.RedisToken;
import com.github.tonivade.resp.protocol.SafeString;
import com.zfx.data.DatabaseValue;


public class DBResponse {

    public static RedisToken convertValue(DatabaseValue value) {
        if (value != null) {
            switch (value.getType()) {
                case STRING:
                    SafeString string = value.getString();
                    return RedisToken.string(string);
            }
        }
        return RedisToken.nullString();
    }
}
