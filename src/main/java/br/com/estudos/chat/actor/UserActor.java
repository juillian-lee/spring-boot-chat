package br.com.estudos.chat.actor;

import javax.websocket.Session;

import akka.actor.Props;
import br.com.estudos.chat.component.ActorFactory;
import br.com.estudos.chat.tcp.ConnectionActor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import br.com.estudos.chat.SpringExtension;

import java.util.Iterator;

@Component("userActor")
@Scope("prototype")
public class UserActor extends AbstractActor {

	@Autowired
    private SpringExtension springExtension;

	@Autowired
	private ActorFactory actorFactory;

    private void receiveMessage(String s) throws Exception {
        ActorRef messageRouter = actorFactory.getActorRef(MessageRouter.class, "messageRouter");
        messageRouter.tell(s, getSelf());
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

	public static class SendMessage {

    }

	@Override
	public Receive createReceive() {
		return receiveBuilder()
                .match(MessageRouter.AddConnection.class, addConnection -> {
                    String name = String.valueOf(addConnection.actor.path().uid());
                    ActorRef actorRef = getContext().actorOf(Props.create(UserChildrenActor.class), name);
                    actorRef.tell(addConnection, getSelf());
                })
				.match(String.class, this::receiveMessage)
                .match(SendMessage.class, sendMessage -> {
                    System.out.println("Chegou aqui");
                })
		        .build();
	}

}
