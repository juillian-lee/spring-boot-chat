package br.com.estudos.chat.actor;

import javax.websocket.Session;

import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;
import akka.util.ByteStringBuilder;
import br.com.estudos.chat.action.response.LoginResponse;
import br.com.estudos.chat.action.response.Response;
import br.com.estudos.chat.entity.Usuario;
import br.com.estudos.chat.protocol.RawMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

@Component("userActor")
@Scope("prototype")
public class UserActor extends AbstractActor {

    @Autowired
    private ActorFactory actorFactory;

    @Autowired
    ObjectMapper mapper;


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MessageRouter.LoginSuccess.class, loginSuccess -> {
                    String name = String.valueOf(getSender().path().uid());
                    ActorRef actorRef = actorFactory.getActorRef(getContext(), UserChildrenActor.class, name);
                    actorRef.tell(getSender(), getSelf());

                    LoginResponse loginResponse = new LoginResponse(getSelf(), actorRef, "LOGIN_SUCCESS");
                    loginResponse.setMessage("Login realizado com sucesso.");

                    getSelf().tell(loginResponse, ActorRef.noSender());
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
                    actorTo.tell(write, actorFrom);
                })
                .build();
    }

}
