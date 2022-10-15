package com.zfx.command;

import java.util.Collections;
import java.util.List;

public class Request implements IRequest {

    private String command;

    private List<String> params;

    private static final String DELIMITER = "\r\n";

    public Request(String command, List<String> params) {
        this.command = command;
        this.params = params;
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public String getParam(int i) {
        return params.get(i);
    }

    @Override
    public int getLength() {
        return params.size();
    }

    @Override
    public List<String> getParams() {
        return Collections.unmodifiableList(params);
    }


    @Override
    public String toString() {
        return command + "[" + params.size() + "]" + params;
    }
}
