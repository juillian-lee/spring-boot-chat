package br.com.estudos.chat.action.response;

import akka.actor.ActorRef;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Response {

    @JsonIgnore
    protected ActorRef actorFrom;

    @JsonIgnore
    protected ActorRef actorTo;
    protected String code;


    public Response(ActorRef actorFrom, ActorRef actorTo, String code) {
        this.actorFrom = actorFrom;
        this.actorTo = actorTo;
        this.code = code;
    }

    public ActorRef getActorFrom() {
        return actorFrom;
    }

    public void setActorFrom(ActorRef actorFrom) {
        this.actorFrom = actorFrom;
    }

    public ActorRef getActorTo() {
        return actorTo;
    }

    public void setActorTo(ActorRef actorTo) {
        this.actorTo = actorTo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
