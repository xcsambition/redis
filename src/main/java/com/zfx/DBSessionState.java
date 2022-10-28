package com.zfx;

public class DBSessionState {

    private int db;

    public DBSessionState() {

    }

    public DBSessionState(int db) {
        this.db = db;
    }

    public int getCurrentDB() {
        return db;
    }

    public void setCurrentDB(int id) {
        this.db = db;
    }
}
