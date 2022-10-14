package com.zfx.command.impl;

import com.zfx.command.IRequest;
import com.zfx.command.IResponse;
import com.zfx.data.IDatabase;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import static org.mockito.Mockito.verify;


public class FlushDBCommandTest {

    @Mock
    private IDatabase db;

    @Mock
    private IRequest request;

    @Mock
    private IResponse response;

    @Test
    public void testExecute() {
        FlushDBCommand command = new FlushDBCommand();

        command.execute(db, request, response);

        verify(response).addSimpleStr("OK");
    }

}
