package br.com.estudos.chat.action.response;

import akka.actor.ActorRef;

public class LoginResponse extends Response{
    private String message;

    public LoginResponse(ActorRef actorFrom, ActorRef actorTo, String code) {
        super(actorFrom, actorTo, code);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
