package br.com.estudos.chat.actor;

import javax.websocket.Session;

import akka.actor.ActorRef;
import br.com.estudos.chat.SpringExtension;
import br.com.estudos.chat.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.AbstractActor;

@Component("webActor")
@Scope("prototype")
public class WebActor extends AbstractActor {

    @Autowired
    BusinessService businessService;

    private Session session;

    public static ActorRef create(ActorContext actorContext, SpringExtension springExtension, Session session) {
        return actorContext.actorOf(springExtension.props("webActor"), session.getId());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Session.class, session -> {
                    this.session = session;
                }).match(String.class, message -> {
                    if (session == null || !session.isOpen()) {
                        getContext().stop(getSelf());
                        return;
                    }
                    session.getBasicRemote().sendText(message);
                }).build();
    }
}
