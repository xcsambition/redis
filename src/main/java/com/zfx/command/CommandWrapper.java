package com.zfx.command;

import com.zfx.data.IDatabase;

public class CommandWrapper implements ICommand {

    private int params;
    private ICommand command;
    public CommandWrapper(ICommand command, int params) {
        this.params = params;
        this.command = command;
    }


    @Override
    public void execute(IDatabase db, IRequest request, IResponse iResponse) {
        if (request.getLength() < params) {
            iResponse.addError("ERR wrong number of arguments for '" + request.getCommand() + "' command");
        }else {
            command.execute(db,request,iResponse);
        }


    }
}
