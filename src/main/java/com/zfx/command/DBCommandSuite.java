package com.zfx.command;

import com.github.tonivade.purefun.Producer;
import com.github.tonivade.resp.command.CommandSuite;
import com.zfx.command.string.GetCommand;
import com.zfx.command.string.SetCommand;

public class DBCommandSuite extends CommandSuite {

    public DBCommandSuite() {
        super(new DBCommandWrapperFactory());
        addCommand(new Producer<GetCommand>() {
            @Override
            public GetCommand run() throws Throwable {
                return new GetCommand();
            }
        });
        addCommand(SetCommand::new);
    }
}
