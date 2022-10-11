package com.zfx.command;

import java.util.List;

public class Request implements IRequest{

    private String command;

    private List<String> params;

    private static final String DELIMITER = "\r\n";

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
        return params;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return command + "[" + params.size() + "]" + params;
    }
}
