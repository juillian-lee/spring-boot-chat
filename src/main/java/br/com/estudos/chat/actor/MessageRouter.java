package br.com.estudos.chat.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import br.com.estudos.chat.component.ActorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("messageRouter")
@Scope("prototype")
public class MessageRouter extends AbstractActor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ActorFactory actorFactory;

    public static class AddConnection {
        public final ActorRef actor;

        public AddConnection(ActorRef actor) {
            this.actor = actor;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(AddConnection.class, addConnection -> {
                    ActorRef actorRef = actorFactory.getActorRef(UserActor.class, "1");
                    actorRef.tell(addConnection, getSelf());
                })
                .match(String.class, s -> {
                    log.debug(">>> [MessageRouter]");
                    getSender().tell(new UserActor.SendMessage(), getSelf());
                    log.debug(">>> Fim do MessageRouter");
                })
                .build();
    }
}
