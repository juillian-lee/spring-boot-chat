package br.com.estudos.chat.action.response;

import akka.actor.ActorRef;

public class MessageResponse extends Response {
    private String mensagem;
    private String identifier;
    private String msgId;


    public MessageResponse(ActorRef actorFrom, ActorRef actorTo, String code) {
        super(actorFrom, actorTo, code);
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
