package br.com.estudos.chat.socket;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import br.com.estudos.chat.actor.WebActor;
import br.com.estudos.chat.protocol.RawMessage;
import org.springframework.beans.factory.annotation.Autowired;

import akka.actor.ActorRef;
import br.com.estudos.chat.actor.UserActor;
import br.com.estudos.chat.actor.UserActor.AddConnection;
import br.com.estudos.chat.component.ActorFactory;

@ServerEndpoint(value="/chat/{id}",
        configurator = CustomSpringConfigurator.class,
        encoders = WebSocketChatEncoder.class,
        decoders = WebSocketChatDecoder.class
)
public class ServerEndPoint {
	
	@Autowired
	ActorFactory actorFactory;

	@OnOpen
	public void open(final Session session, @PathParam("id") String id) {
		try {
			ActorRef actorRef = actorFactory.getActorRef(WebActor.class, session.getId());
			actorRef.tell(session, ActorRef.noSender());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@OnMessage
    public void onMessage(RawMessage rawMessage, Session session, @PathParam("id") String id) throws Exception {
		ActorRef actorRef = actorFactory.getActorRef(WebActor.class, session.getId());
		actorRef.tell(rawMessage, ActorRef.noSender());
    }

    @OnClose
    public void onClose(Session session) throws Exception {
        ActorRef actorRef = actorFactory.getActorRef(WebActor.class, session.getId());
        actorRef.tell("stop", ActorRef.noSender());
    }

}
