package br.com.estudos.chat.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

public class UserChildrenActor extends AbstractActor {

    private ActorRef actorSender;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("stop", s -> {
                    getContext().stop(getSelf());
                })
                .match(MessageRouter.AddConnection.class, addConnection -> {
                    this.actorSender = addConnection.actor;
                    WebActor.AddUserPath addUserPath = new WebActor.AddUserPath(getSelf().path().toString());
                    this.actorSender.tell(addUserPath, getSelf());
                })
                .build();
    }
}
