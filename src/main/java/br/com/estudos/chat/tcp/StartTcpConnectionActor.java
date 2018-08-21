package br.com.estudos.chat.tcp;


import java.net.InetSocketAddress;
import akka.actor.ActorRef;
import akka.actor.AbstractActor;
import akka.io.Tcp;
import akka.io.TcpMessage;
import br.com.estudos.chat.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("startTcpActor")
@Scope("prototype")
public class StartTcpConnectionActor extends AbstractActor {

    @Autowired
    SpringExtension springExtension;

    private void start(Start start) {
        final ActorRef tcpManager = Tcp.get(getContext().getSystem()).manager();
        tcpManager.tell(TcpMessage.bind(getSelf(), new InetSocketAddress(9091), 100), getSelf());
    }

    public static class Start {}

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Start.class, this::start)
                .match(Tcp.Connected.class, msg -> {
                    ActorRef tcpActor = getContext().actorOf(springExtension.props("tcpActor"), String.valueOf(getSender().path().uid()));
                    getSender().tell(TcpMessage.register(tcpActor), getSelf());
                })
                .build();
    }
}
