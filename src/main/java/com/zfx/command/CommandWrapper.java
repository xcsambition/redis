package com.zfx.command;

import com.zfx.annoation.ParamLength;
import com.zfx.annoation.ParamType;
import com.zfx.data.DataType;
import com.zfx.data.IDatabase;

public class CommandWrapper implements ICommand {

    private int params;

    private DataType dataType;
    private ICommand command;

    public CommandWrapper(ICommand command) {
        ParamLength length = command.getClass().getAnnotation(ParamLength.class);
        if (length != null) {
            this.params = length.value();
        }
        ParamType type = command.getClass().getAnnotation(ParamType.class);
        if (type != null) {
            this.dataType = type.value();
        }
        this.command = command;
    }


    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        if (request.getLength() < params) {
            response.addError("ERR wrong number of arguments for '" + request.getCommand() + "' command");
        } else if (dataType != null && !db.isType(request.getParam(0), dataType)) {
            response.addError("WRONGTYPE Operation against a key holding the wrong kind of value");
        } else {
            command.execute(db, request, response);
        }


    }
}
