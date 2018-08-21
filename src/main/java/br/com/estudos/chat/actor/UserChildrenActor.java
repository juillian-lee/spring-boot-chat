package br.com.estudos.chat.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import br.com.estudos.chat.component.ActorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("userChildrenActor")
@Scope("prototype")
public class UserChildrenActor extends AbstractActor {

    private ActorRef actorSender;

    @Autowired
    ActorFactory actorFactory;

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
