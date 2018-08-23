package br.com.estudos.chat.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import br.com.estudos.chat.action.StopActor;
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
                .matchEquals(StopActor.class, s -> {
                    getContext().stop(getSelf());
                })
                .match(ActorRef.class, actorSender -> {
                    this.actorSender = actorSender;
                    this.actorSender.tell(getSelf(), ActorRef.noSender());
                })
                .build();
    }
}
