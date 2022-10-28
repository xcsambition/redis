package com.zfx.data;

import com.github.tonivade.resp.protocol.SafeString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

class DefaultDatabaseTest {

    @Test
    void get() {
        DefaultDatabase db = new DefaultDatabase(new HashMap<>());
        DatabaseValue value = db.get(DatabaseKey.safeKey(SafeString.safeString("a")));
        Assertions.assertEquals(null,value);
    }

    @Test
    void put() {
        DefaultDatabase db = new DefaultDatabase(new HashMap<>());
        DatabaseValue value = db.get(DatabaseKey.safeKey(SafeString.safeString("a")));
        Assertions.assertEquals(null,value);

        value = db.put((DatabaseKey.safeKey(SafeString.safeString("a"))), DatabaseValue.string("b"));
        Assertions.assertEquals(null,value);
        value = db.put((DatabaseKey.safeKey(SafeString.safeString("a"))), DatabaseValue.string("c"));
        Assertions.assertEquals(DatabaseValue.string("b"),value);

    }
}