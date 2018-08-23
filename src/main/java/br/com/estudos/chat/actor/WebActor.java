package br.com.estudos.chat.actor;

import javax.websocket.Session;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import br.com.estudos.chat.action.StopActor;
import br.com.estudos.chat.component.ActorFactory;
import br.com.estudos.chat.protocol.RawMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component("webActor")
@Scope("prototype")
public class WebActor extends AbstractActor {

    @Autowired
    private ActorFactory actorFactory;

    ActorRef messageRouter;
    private Session session;
    private ActorRef userChildren;

    @Override
    public void preStart() throws Exception {
        messageRouter = actorFactory.getActorRef(MessageRouter.class, "messageRouter");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ActorRef.class, this::addActorChildren)
                .matchEquals(StopActor.class, this::stopActor)
                .match(Session.class, this::addSessionWebSocket)
                .match(RawMessage.class, this::rawMessageReceive)
                .build();
    }

    /**
     * Metodo que recebe o ator criado para o children do usuario
     * o ator que sera responsavel por esta conexao do usuario
     *
     * @param userChildren
     */
    private void addActorChildren(ActorRef userChildren) {
        this.userChildren = userChildren;
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
     * @param rawMessage
     */
    private void rawMessageReceive(RawMessage rawMessage) {
        messageRouter.tell(rawMessage, getSelf());
    }

    /**
     * Para o actor e destroy as referencias criadas para
     * a comunicação com este ator.
     *
     * @param stopActorClass
     */
    private void stopActor(Class<StopActor> stopActorClass) {
        if (userChildren != null) {
            userChildren.tell(StopActor.class, ActorRef.noSender());
        }
        getContext().stop(getSelf());
    }
}
