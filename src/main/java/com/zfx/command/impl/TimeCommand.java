package com.zfx.command.impl;

import com.zfx.command.ICommand;
import com.zfx.command.IRequest;
import com.zfx.command.IResponse;
import com.zfx.data.IDatabase;

import java.util.LinkedList;
import java.util.List;

public class TimeCommand implements ICommand {
    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        List<String> result = new LinkedList<>();
        long currentTimeMillis = System.currentTimeMillis();
        result.add(String.valueOf(currentTimeMillis/1000));
        result.add(String.valueOf(currentTimeMillis%1000));
        response.addArray(result);
    }
}
