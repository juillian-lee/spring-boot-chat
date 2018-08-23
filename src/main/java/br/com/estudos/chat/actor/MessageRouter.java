package br.com.estudos.chat.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import br.com.estudos.chat.action.Action;
import br.com.estudos.chat.action.ActionFactory;
import br.com.estudos.chat.action.LoginAction;
import br.com.estudos.chat.entity.Usuario;
import br.com.estudos.chat.protocol.RawMessage;
import br.com.estudos.chat.repository.UsuarioRepository;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
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


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RawMessage.class, this::rawMessage)
                .match(LoginAction.class, this::loginAction)
                .build();
    }

    /**
     * Converte o rawMessage para uma Action com base no code
     *
     * @param rawMessage
     * @throws IOException
     * @throws JSONException
     */
    private void rawMessage(RawMessage rawMessage) throws IOException, JSONException {
        String json = rawMessage.getText();
        Action action = actionFactory.fromJSON(json);
        if (action != null) {
            getSelf().tell(action, getSender());
        }
    }

    /**
     * Realiza o login do usuario no socket
     *
     * @param loginAction
     * @throws Exception
     */
    private void loginAction(LoginAction loginAction) throws Exception {
        Optional<Usuario> usuarioOptional = usuarioRepository.login(loginAction.getLogin(), loginAction.getPassword());
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            ActorRef actorRef = actorFactory.getActorRef(UserActor.class, String.valueOf(usuario.getId()));
            actorRef.tell(usuario, getSender());
        }
    }
}
