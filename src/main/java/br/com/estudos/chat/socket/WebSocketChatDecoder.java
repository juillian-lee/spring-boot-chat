package br.com.estudos.chat.socket;

import br.com.estudos.chat.protocol.RawMessage;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class WebSocketChatDecoder implements Decoder.Text<RawMessage> {
    @Override
    public void init(final EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public RawMessage decode(final String json) throws DecodeException {
        RawMessage rawMessage = RawMessage.apply(json);
        return rawMessage;
    }

    @Override
    public boolean willDecode(final String s) {
        return true;
    }
}
