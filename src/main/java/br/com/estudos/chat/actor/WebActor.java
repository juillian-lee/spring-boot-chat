package br.com.estudos.chat.actor;

import javax.websocket.Session;

import akka.actor.*;
import akka.pattern.AskableActorSelection;
import akka.util.Timeout;
import br.com.estudos.chat.component.ActorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import scala.concurrent.Await;
import scala.concurrent.Future;

import java.util.concurrent.TimeUnit;

@Component("webActor")
@Scope("prototype")
public class WebActor extends AbstractActor {

    @Autowired
    private ActorFactory actorFactory;

    private Session session;
    private String userChildrenPath;


    public static class AddUserPath {
        public final String path;

        public AddUserPath(String path) {
            this.path = path;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(AddUserPath.class, addUserPath -> {
                    this.userChildrenPath = addUserPath.path;
                })
                .matchEquals("stop", s -> {
                    ActorRef actor = actorFactory.getActor(this.userChildrenPath);
                    if(actor != null) {
                        actor.tell("stop", ActorRef.noSender());
                    }
                    getContext().stop(getSelf());
                })
                .match(Session.class, session -> {
                    this.session = session;
                }).match(String.class, message -> {
                    ActorRef messageRouter = actorFactory.getActorRef(MessageRouter.class, "messageRouter");
                    messageRouter.tell(new MessageRouter.AddConnection(getSelf()), getSelf());
                }).build();
    }
}
