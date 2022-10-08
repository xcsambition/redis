package com.zfx.command;

import java.util.List;

public interface IRequest {

    String getCommand();

    String getParam(int i);

    int getLength();

    List<String> getParams();


}
