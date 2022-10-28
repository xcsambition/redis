package com.zfx;

import com.zfx.data.Database;
import com.zfx.data.DefaultDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBServerState {


    private final List<Database> databases = new ArrayList<>();

    public DBServerState() {
        for (int i = 0; i < 10; i++) {
            databases.add(new DefaultDatabase(new HashMap<>()));
        }
    }


    public Database getDatabase(int id) {
        return databases.get(id);
    }
}
