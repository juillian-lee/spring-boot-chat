package br.com.estudos.chat.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import br.com.estudos.chat.action.Action;
import br.com.estudos.chat.action.ActionFactory;
import br.com.estudos.chat.action.LoginAction;
import br.com.estudos.chat.component.ActorFactory;
import br.com.estudos.chat.entity.Usuario;
import br.com.estudos.chat.protocol.RawMessage;
import br.com.estudos.chat.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("messageRouter")
@Scope("prototype")
public class MessageRouter extends AbstractActor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ActorFactory actorFactory;

    @Autowired
    ActionFactory actionFactory;

    @Autowired
    UsuarioRepository usuarioRepository;

    public static class AddConnection {
        public final ActorRef actor;

        public AddConnection(ActorRef actor) {
            this.actor = actor;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RawMessage.class, rawMessage -> {
                    String json = rawMessage.getText();
                    Action action = actionFactory.fromJSON(json);
                    if(action != null) {
                        getSelf().tell(action, getSender());
                    }
                })
                .match(LoginAction.class, loginAction -> {
                    Optional<Usuario> usuarioOptional = usuarioRepository.login(loginAction.getLogin(), loginAction.getPassword());
                    if(usuarioOptional.isPresent()) {
                        Usuario usuario = usuarioOptional.get();
                        ActorRef actorRef = actorFactory.getActorRef(UserActor.class, String.valueOf(usuario.getId()));
                        actorRef.tell(usuario, getSender());
                    }
                })
                .match(AddConnection.class, addConnection -> {
                    ActorRef actorRef = actorFactory.getActorRef(UserActor.class, "1");
                    actorRef.tell(addConnection, getSelf());
                })
                .build();
    }
}
