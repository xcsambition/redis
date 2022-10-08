package com.zfx.command;

import com.zfx.data.Database;
import com.zfx.data.IDatabase;

public interface ICommand {
    String OK = "OK";

    String ERROR = "ERR";


    void execute(IDatabase db, IRequest request, IResponse response);
}
