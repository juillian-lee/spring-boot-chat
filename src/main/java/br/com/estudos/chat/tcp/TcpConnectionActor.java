package br.com.estudos.chat.tcp;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.io.Tcp;
import br.com.estudos.chat.actor.UserActor;
import br.com.estudos.chat.component.ActorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import scala.collection.immutable.Iterable;

/**

 TCP - WEB >>>>> USER



 */
@Component("tcpActor")
@Scope("prototype")
public class TcpConnectionActor extends AbstractActor {

    @Autowired
    ActorFactory actorFactory;

    public ActorRef tcpConnection;

    public TcpConnectionActor() {

    }

    public TcpConnectionActor(ActorRef sender) {
        this.tcpConnection = sender;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Tcp.Received.class, received -> {
                    System.out.println(getContext());
                    System.out.println(getSender());
                    System.out.println(getSelf());
                    ActorRef actorRef = actorFactory.getActorRef(UserActor.class, "1");
                    actorRef.tell(new UserActor.AddTcpConnection(getSelf()), ActorRef.noSender());
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