package br.com.estudos.chat.actor;

import javax.websocket.Session;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.io.Tcp;
import br.com.estudos.chat.action.StopActor;
import br.com.estudos.chat.protocol.RawMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component("webActor")
@Scope("prototype")
public class WebActor extends AbstractActor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ActorFactory actorFactory;

    ActorRef messageRouter;
    private Session session;
    private ActorRef userChildrenReference;

    @Override
    public void preStart() throws Exception {
        messageRouter = actorFactory.getActorRef(MessageRouter.class, "messageRouter");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RawMessage.class, this::rawMessageReceive)
                .match(ActorRef.class, this::addActorUserChildrenReference)
                .match(Session.class, this::addSessionWebSocket)
                .match(StopActor.class, this::stopActor)
                .match(Tcp.Write.class, this::writeMessage)
                .build();
    }

    /**
     * Metodo que recebe o ator criado para o children do usuario
     * o ator que sera responsavel por esta conexao do usuario
     *
     * @param userChildrenReference
     */
    private void addActorUserChildrenReference(ActorRef userChildrenReference) {
        this.userChildrenReference = userChildrenReference;
    }

    /**
     * Adiciona a sessao do webSocket no actor
     *
     * @param session
     */
    private void addSessionWebSocket(Session session) {
        this.session = session;
    }

    /**
     * Recebe uma rawMessage e passa para o userMessageRouter
     *
     * @param rawMessage
     */
    private void rawMessageReceive(RawMessage rawMessage) {
        log.debug(">>> Chegou o rawMessage" + rawMessage);
        log.debug(">> userChildrenReference" + this.userChildrenReference);
        log.debug(">> self" + getSelf());
        messageRouter.tell(rawMessage, this.userChildrenReference == null ? getSelf() : this.userChildrenReference);
    }

    /**
     * Para o actor e destroy as referencias criadas para
     * a comunicação com este ator.
     *
     * @param stopActorClass
     */
    private void stopActor(StopActor stopActorClass) {
        if (userChildrenReference != null) {
            userChildrenReference.tell(new StopActor(), ActorRef.noSender());
        }
        getContext().stop(getSelf());
    }

    private void writeMessage(Tcp.Write write) throws IOException {
        if (session != null && session.isOpen()) {
            byte[] bytes = write.data().toArray();
            RawMessage rawMessage = new RawMessage(bytes);
            session.getBasicRemote().sendText(rawMessage.toString());
            return;
        }
        getSelf().tell(StopActor.class, getSelf());
    }
}
