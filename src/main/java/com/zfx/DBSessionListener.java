package com.zfx;

import com.github.tonivade.resp.SessionListener;
import com.github.tonivade.resp.command.Session;

public class DBSessionListener implements SessionListener {
    @Override
    public void sessionDeleted(Session session) {
        session.destroy();
    }

    @Override
    public void sessionCreated(Session session) {
        session.putValue("state", new DBSessionState());
    }
}