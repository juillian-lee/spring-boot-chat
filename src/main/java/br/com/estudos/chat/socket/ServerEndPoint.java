package br.com.estudos.chat.socket;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;

import akka.actor.ActorRef;
import br.com.estudos.chat.actor.UserActor;
import br.com.estudos.chat.actor.WorkerActor;
import br.com.estudos.chat.actor.UserActor.AddConnection;
import br.com.estudos.chat.component.ActorFactory;

@ServerEndpoint(value="/chat/{id}", configurator = CustomSpringConfigurator.class)
public class ServerEndPoint {
	
	@Autowired
	ActorFactory actorFactory;
	
	@OnOpen
	public void open(final Session wsSession, @PathParam("id") String id) {
		try {
			ActorRef actorRef = actorFactory.getActorRef(UserActor.class, id);
			actorRef.tell(new AddConnection(wsSession), ActorRef.noSender());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@OnMessage
    public void onMessage(String message, Session session, @PathParam("id") String id) throws Exception {
		
    }
	
}
