package br.com.estudos.chat.action.response;

import akka.actor.ActorRef;

public class MessageStatus extends Response {

    private Long msgId;
    private Long identifier;

    public MessageStatus(ActorRef actorFrom, ActorRef actorTo, String code) {
        super(actorFrom, actorTo, code);
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }
}
