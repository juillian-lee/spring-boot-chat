package br.com.estudos.chat.socket;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import br.com.estudos.chat.action.StopActor;
import br.com.estudos.chat.actor.WebActor;
import br.com.estudos.chat.protocol.RawMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import akka.actor.ActorRef;
import br.com.estudos.chat.actor.ActorFactory;

@ServerEndpoint(value="/chat/{id}",
        configurator = CustomSpringConfigurator.class,
        encoders = WebSocketChatEncoder.class,
        decoders = WebSocketChatDecoder.class
)
public class ServerEndPoint {

    private final String PREFIX = "WEB";
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ActorFactory actorFactory;

	@OnOpen
	public void open(final Session session, @PathParam("id") String id) throws Exception {
		log.debug(">>> Iniciando a conexao do socket");
        ActorRef actorRef = getActor(session);

        log.debug(">>> conexao com o akka", session.getId());
		actorRef.tell(session, ActorRef.noSender());
		log.debug(">>> ActorRef" + actorRef);
	}
	
	@OnMessage
    public void onMessage(RawMessage rawMessage, Session session, @PathParam("id") String id) throws Exception {
        ActorRef actorRef = getActor(session);
		log.debug(">>> conexao com o akka", session.getId());
		log.debug(">>> ActorRef" + actorRef);
		log.debug("raw" + rawMessage);
		actorRef.tell(rawMessage, ActorRef.noSender());
    }

    @OnClose
    public void onClose(Session session) throws Exception {
        ActorRef actorRef = getActor(session);
        actorRef.tell(new StopActor(), ActorRef.noSender());
    }

    private ActorRef getActor(Session session) throws Exception {
        ActorRef actorRef = actorFactory.getActorRef(WebActor.class, PREFIX + session.getId());
        return actorRef;
    }

}
