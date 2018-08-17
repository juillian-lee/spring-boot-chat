package br.com.estudos.chat.component;

import java.util.concurrent.TimeUnit;

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
import br.com.estudos.chat.actor.WorkerActor;
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
			if(WorkerActor.class.equals(clazz)) {
				return actorSystem.actorOf(springExtension.props("workerActor"), actorName);
			}
			
			if(UserActor.class.equals(clazz)) {
				return actorSystem.actorOf(springExtension.props("userActor"), actorName);
			}
			
			throw new IllegalArgumentException("Bean " + clazz + " nao mappeado");
		});
	}
	
}
