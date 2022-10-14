package com.zfx.command.impl;

import com.zfx.command.ICommand;
import com.zfx.command.IRequest;
import com.zfx.command.IResponse;
import com.zfx.data.IDatabase;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;


import static org.mockito.Mockito.verify;


public class PingCommandTest {

    @Mock
    private IDatabase db;

    @Mock
    private IRequest request;

    @Mock
    private IResponse response;

    @Test
    public void testExecute() {
        ICommand command = new PingCommand();

        command.execute(db, request, response);

        verify(response).addSimpleStr("PONG");
    }


}
