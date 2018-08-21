package br.com.estudos.chat.component;

import java.util.concurrent.TimeUnit;

import br.com.estudos.chat.actor.MessageRouter;
import br.com.estudos.chat.actor.UserChildrenActor;
import br.com.estudos.chat.actor.WebActor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import akka.actor.ActorIdentity;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Identify;
import akka.pattern.AskableActorSelection;
import akka.util.Timeout;
import br.com.estudos.chat.SpringExtension;
import br.com.estudos.chat.actor.UserActor;
import scala.concurrent.Await;
import scala.concurrent.Future;

@Component
public class ActorFactory {
	
	@Autowired
    private ActorSystem actorSystem;

    @Autowired
    private SpringExtension springExtension;
	
	public ActorRef getActorRef(Object clazz, String actorName) throws Exception {
		ActorSelection actorSelection = actorSystem.actorSelection("/user/" + actorName);
        Timeout timeout = new Timeout(5, TimeUnit.SECONDS);
        AskableActorSelection asker = new AskableActorSelection(actorSelection);
        Future<Object> fut = asker.ask(new Identify(1), timeout);
        ActorIdentity ident = (ActorIdentity) Await.result(fut, timeout.duration());
		return ident.getActorRef().orElseGet(() -> {

			if(UserActor.class.equals(clazz)) {
				return actorSystem.actorOf(springExtension.props("userActor"), actorName);
			}

			if(MessageRouter.class.equals(clazz)) {
				return actorSystem.actorOf(springExtension.props("messageRouter"), actorName);
			}

			if(WebActor.class.equals(clazz)) {
                return actorSystem.actorOf(springExtension.props("webActor"), actorName);
            }

            if(UserChildrenActor.class.equals(clazz)) {
                return actorSystem.actorOf(springExtension.props("userChildrenActor"), actorName);
            }

			throw new IllegalArgumentException("Bean " + clazz + " nao mappeado");
		});
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
