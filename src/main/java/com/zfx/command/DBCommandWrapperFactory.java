package com.zfx.command;

import com.github.tonivade.resp.command.CommandWrapperFactory;
import com.github.tonivade.resp.command.RespCommand;

public class DBCommandWrapperFactory implements CommandWrapperFactory {
    @Override
    public RespCommand wrap(Object command) {
        return new DBCommandWrapper(command);
    }
}
