package br.com.estudos.chat.socket;

import br.com.estudos.chat.protocol.RawMessage;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class WebSocketChatEncoder implements Encoder.Text<RawMessage> {

    @Override
    public void init(final EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public String encode(final RawMessage raw) throws EncodeException {
        String json = raw.getText();
        return json;
    }
}
