package br.com.estudos.chat.actor;

import javax.websocket.Session;

import br.com.estudos.chat.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

@Component("userActor")
@Scope("prototype")
public class UserActor extends AbstractActor {

	@Autowired
	private ActorFactory actorFactory;


    static public class AddConnection {
		public final Session session;

		public AddConnection(Session session) {
			this.session = session;
		}
	}

	static public class AddTcpConnection {
    	public final ActorRef actorRef;

		public AddTcpConnection(ActorRef actorRef) {
			this.actorRef = actorRef;
		}
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
                .match(Usuario.class, usuario -> {
                    String name = String.valueOf(getSender().path().uid());
                    ActorRef actorRef = actorFactory.getActorRef(getContext(), UserChildrenActor.class, name);
                    actorRef.tell(getSender(), getSelf());
                })
		        .build();
	}

}
