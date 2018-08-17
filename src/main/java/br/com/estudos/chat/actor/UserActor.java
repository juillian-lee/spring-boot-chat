package br.com.estudos.chat.actor;

import javax.websocket.Session;

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

//	ActorRef create(Session session) {
//		return getContext().actorOf(springExtension.props("webActor"), session.getId());
//	}

	static public class AddConnection {
		public final Session session;

		public AddConnection(Session session) {
			this.session = session;
		}
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
		        .match(AddConnection.class, this::addConnection)
		        .build();
	}

}
