package br.com.estudos.chat.actor;

import javax.websocket.Session;

import akka.actor.Props;
import br.com.estudos.chat.tcp.ConnectionActor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import br.com.estudos.chat.SpringExtension;

@Component("userActor")
@Scope("prototype")
public class UserActor extends AbstractActor {

	@Autowired
    private SpringExtension springExtension;

    private void addConnection(AddConnection addConection) {
        ActorRef actorRef = WebActor.create(getContext(), springExtension, addConection.session);
        actorRef.tell(addConection.session, ActorRef.noSender());
    }

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
		        .match(AddConnection.class, this::addConnection)
				.match(AddTcpConnection.class, addTcpConnection -> {
					ActorRef actorRef = getContext().actorOf(Props.create(ConnectionActor.class, addTcpConnection.actorRef));
                    actorRef.tell("teste", ActorRef.noSender());
				})
		        .build();
	}

}
