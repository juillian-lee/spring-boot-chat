package br.com.estudos.chat;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import br.com.estudos.chat.entity.Usuario;
import br.com.estudos.chat.repository.UsuarioRepository;
import br.com.estudos.chat.tcp.StartTcpConnectionActor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationListener implements org.springframework.context.ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    SpringExtension springExtension;

    @Autowired
    ActorSystem actorSystem;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        List<Usuario> usuarios = usuarioRepository.findAll();
        if(usuarios.size() == 0) {
            Usuario usuario1 = new Usuario();
            usuario1.setLogin("juillian1");
            usuario1.setPassword("juillian1");
            usuario1.setNome("juillian1");
            usuarioRepository.save(usuario1);

            Usuario usuario2 = new Usuario();
            usuario1.setLogin("juillian2");
            usuario1.setPassword("juillian2");
            usuario1.setNome("juillian2");
            usuarioRepository.save(usuario2);
        }

        actorSystem.actorOf(springExtension.props("messageRouter"), "messageRouter");

        ActorRef startTcpActor = actorSystem.actorOf(springExtension.props("startTcpActor"), "tcp");
        startTcpActor.tell(new StartTcpConnectionActor.Start(), ActorRef.noSender());
    }
}
