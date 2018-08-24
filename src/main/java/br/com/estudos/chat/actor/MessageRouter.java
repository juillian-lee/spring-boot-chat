package br.com.estudos.chat.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import br.com.estudos.chat.action.Action;
import br.com.estudos.chat.action.ActionFactory;
import br.com.estudos.chat.action.LoginAction;
import br.com.estudos.chat.action.MessageAction;
import br.com.estudos.chat.action.response.MessageResponse;
import br.com.estudos.chat.action.response.MessageStatus;
import br.com.estudos.chat.action.response.ReplicateOtherConnectionsResponse;
import br.com.estudos.chat.action.response.Response;
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


    public static class LoginSuccess {
        public final Usuario usuario;

        public LoginSuccess(Usuario usuario) {
            this.usuario = usuario;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RawMessage.class, this::rawMessage)
                .match(LoginAction.class, this::loginAction)
                .match(MessageAction.class, messageAction -> {
                    MessageResponse msgResponse = new MessageResponse(getSelf(), getSender(), "msg");
                    msgResponse.setIdentifier("xxxx");
                    msgResponse.setMsgId("asda");
                    ReplicateOtherConnectionsResponse replicate = new ReplicateOtherConnectionsResponse(msgResponse);
                    getSender().tell(replicate, getSelf());

                })
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
        log.debug(">>> Realizando Login");
        Optional<Usuario> usuarioOptional = usuarioRepository.login(loginAction.getLogin(), loginAction.getPassword());
        if (usuarioOptional.isPresent()) {
            log.debug(">>> Usuário encontrado");
            Usuario usuario = usuarioOptional.get();
            ActorRef actorRef = actorFactory.getActorRef(UserActor.class, String.valueOf(usuario.getId()));

            LoginSuccess loginSuccess = new LoginSuccess(usuario);
            actorRef.tell(loginSuccess, getSender());
        } else {
            log.debug(">>> Usuário não encontrado");
        }
    }
}
