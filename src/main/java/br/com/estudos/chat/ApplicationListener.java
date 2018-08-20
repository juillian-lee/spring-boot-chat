package br.com.estudos.chat;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import br.com.estudos.chat.tcp.StartTcpConnectionActor;
import br.com.estudos.chat.tcp.TcpConnectionActor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationListener implements org.springframework.context.ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    SpringExtension springExtension;

    @Autowired
    ActorSystem actorSystem;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ActorRef startTcpActor = actorSystem.actorOf(springExtension.props("startTcpActor"), "tcp");
        startTcpActor.tell(new StartTcpConnectionActor.Start(), ActorRef.noSender());
    }
}
