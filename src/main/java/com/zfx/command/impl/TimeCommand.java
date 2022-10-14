package com.zfx.command.impl;

import com.zfx.command.ICommand;
import com.zfx.command.IRequest;
import com.zfx.command.IResponse;
import com.zfx.data.DatabaseValue;
import com.zfx.data.IDatabase;

import java.util.LinkedList;
import java.util.stream.Stream;

public class TimeCommand implements ICommand {

    private static final int SCALE = 1000;

    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        LinkedList<String> list = new LinkedList<>();
        long currentTimeMillis = System.currentTimeMillis();
        Stream<String> longStream = Stream.of(getSeconds(currentTimeMillis),getMicroSeconds(currentTimeMillis));
        response.addArray(list);
    }

    private String getSeconds(long currentTimeMillis) {
        return String.valueOf(currentTimeMillis / SCALE);
    }

    private String getMicroSeconds(long currentTimeMills) {
        return String.valueOf(currentTimeMills % SCALE);
    }
}
