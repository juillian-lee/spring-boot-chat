package br.com.estudos.chat.actor;

import javax.websocket.Session;

import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;
import akka.util.ByteStringBuilder;
import br.com.estudos.chat.action.ActionCode;
import br.com.estudos.chat.action.response.LoginResponse;
import br.com.estudos.chat.action.response.ReplicateOtherConnectionsResponse;
import br.com.estudos.chat.action.response.Response;
import br.com.estudos.chat.entity.Usuario;
import br.com.estudos.chat.protocol.RawMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

@Component("userActor")
@Scope("prototype")
public class UserActor extends AbstractActor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ActorFactory actorFactory;

    @Autowired
    ObjectMapper mapper;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MessageRouter.LoginSuccess.class, loginSuccess -> {
                    log.debug(">>> loginSuccess");
                    String name = String.valueOf(getSender().path().uid());
                    ActorRef actorRef = actorFactory.getActorRef(getContext(), UserChildrenActor.class, name);
                    actorRef.tell(getSender(), getSelf());

                    LoginResponse loginResponse = new LoginResponse(getSelf(), actorRef, ActionCode.LOGIN_SUCCESS);
                    loginResponse.setMessage("Login realizado com sucesso.");


                    log.debug(">>> Envia a resposta para o getSelf()");
                    getSelf().tell(loginResponse, ActorRef.noSender());
                })
                .match(ReplicateOtherConnectionsResponse.class, replicate -> {
                    Iterable<ActorRef> children = getContext().getChildren();
                    children.forEach(actorRef -> {
                        if(!actorRef.equals(getSender())) {
                            Response response = replicate.getResponse();
                            response.setActorTo(actorRef);
                            response.setActorFrom(getSender());
                            getSelf().tell(response, ActorRef.noSender());
                        }
                    });
                })
                .match(Response.class, response -> {
                    String json = mapper.writeValueAsString(response);
                    RawMessage rawMessage = RawMessage.apply(json);

                    byte[] bytes = rawMessage.getBytes();
                    ByteStringBuilder bb = new ByteStringBuilder();
                    bb.putBytes(bytes);
                    ByteString byteString = bb.result();

                    Tcp.Command write = TcpMessage.write(byteString);
                    ActorRef actorTo = response.getActorTo();
                    ActorRef actorFrom = response.getActorFrom();

                    log.debug("Enviar o response", response);
                    log.debug("actorTO:" + actorTo);
                    log.debug("actorFrom:" + actorFrom);

                    actorTo.tell(write, actorFrom);
                })
                .build();
    }

}
