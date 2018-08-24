package br.com.estudos.chat.tcp;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.io.Tcp;
import br.com.estudos.chat.actor.UserActor;
import br.com.estudos.chat.actor.ActorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import scala.collection.immutable.Iterable;

/**

 TCP AND WEB --> Connection -> USER -> CONNECTIONS


 Outra alternativa

 Criar path separados para cada um

 tcp -> user -> connections /tpc/1/1023i19i9
 web -> user -> connections /web/1/1023i19i9



 */
@Component("tcpActor")
@Scope("prototype")
public class ConnectionActor extends AbstractActor {

    @Autowired
    ActorFactory actorFactory;

    public ActorRef tcpConnection;

    public ConnectionActor() {

    }

    public ConnectionActor(ActorRef sender) {
        this.tcpConnection = sender;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Tcp.Received.class, received -> {
                    System.out.println(getContext());
                    System.out.println(getSender());
                    System.out.println(getSelf());
//                    ActorRef actorRef = actorFactory.getActorRef(UserActor.class, "1");
//                    actorRef.tell(new UserActor.AddTcpConnection(getSelf()), ActorRef.noSender());
                })
                .match(String.class, s -> {
                    Iterable<ActorRef> children = getContext().children();
                    System.out.println(getContext());
                    System.out.println(this.tcpConnection);
                    System.out.println(this.actorFactory);
                })
                .build();

    }
}