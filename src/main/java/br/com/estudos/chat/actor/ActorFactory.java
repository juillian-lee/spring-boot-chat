package br.com.estudos.chat.actor;

import java.util.concurrent.TimeUnit;

import akka.actor.*;
import br.com.estudos.chat.actor.MessageRouter;
import br.com.estudos.chat.actor.UserChildrenActor;
import br.com.estudos.chat.actor.WebActor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import akka.pattern.AskableActorSelection;
import akka.util.Timeout;
import br.com.estudos.chat.produces.SpringExtension;
import br.com.estudos.chat.actor.UserActor;
import scala.concurrent.Await;
import scala.concurrent.Future;

@Component
public class ActorFactory {
	
	@Autowired
    private ActorSystem actorSystem;

    @Autowired
    private SpringExtension springExtension;

    private ActorRef orElseGetActorRef(ActorRefFactory context, Object clazz, String actorName) {
        if(UserActor.class.equals(clazz)) {
            return context.actorOf(springExtension.props("userActor"), actorName);
        }

        if(MessageRouter.class.equals(clazz)) {
            return context.actorOf(springExtension.props("messageRouter"), actorName);
        }

        if(WebActor.class.equals(clazz)) {
            return context.actorOf(springExtension.props("webActor"), actorName);
        }

        if(UserChildrenActor.class.equals(clazz)) {
            return context.actorOf(springExtension.props("userChildrenActor"), actorName);
        }

        throw new IllegalArgumentException("Bean " + clazz + " nao mappeado");
    }

	public ActorRef getActorRef(Object clazz, String actorName) throws Exception {
		ActorSelection actorSelection = actorSystem.actorSelection("/user/" + actorName);
        Timeout timeout = new Timeout(5, TimeUnit.SECONDS);
        AskableActorSelection asker = new AskableActorSelection(actorSelection);
        Future<Object> fut = asker.ask(new Identify(1), timeout);
        ActorIdentity ident = (ActorIdentity) Await.result(fut, timeout.duration());
		return ident.getActorRef().orElseGet(() -> orElseGetActorRef(actorSystem, clazz, actorName));
	}

    public ActorRef getActorRef(ActorRefFactory context,Object clazz, String actorName) throws Exception {
        ActorSelection actorSelection = context.actorSelection(actorName);
        Timeout timeout = new Timeout(5, TimeUnit.SECONDS);
        AskableActorSelection asker = new AskableActorSelection(actorSelection);
        Future<Object> fut = asker.ask(new Identify(1), timeout);
        ActorIdentity ident = (ActorIdentity) Await.result(fut, timeout.duration());
        return ident.getActorRef().orElseGet(() -> orElseGetActorRef(context, clazz, actorName));
    }

	public ActorRef getActor(String actorPath) throws Exception {
        ActorSelection actorSelection = actorSystem.actorSelection(actorPath);
        Timeout timeout = new Timeout(5, TimeUnit.SECONDS);
        AskableActorSelection asker = new AskableActorSelection(actorSelection);
        Future<Object> fut = asker.ask(new Identify(1), timeout);
        ActorIdentity ident = (ActorIdentity) Await.result(fut, timeout.duration());
        ActorRef ref = ident.getRef();
        return ref;
    }

}
