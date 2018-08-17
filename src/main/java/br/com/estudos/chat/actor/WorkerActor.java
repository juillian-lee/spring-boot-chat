package br.com.estudos.chat.actor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.AbstractActor;
import br.com.estudos.chat.service.BusinessService;

@Component("workerActor")
@Scope("prototype")
public class WorkerActor extends AbstractActor {

    @Autowired
    private BusinessService businessService;

    private int count = 0;

    public static class Request {
    }

    public static class Response {
    }

	@Override
	public Receive createReceive() {
		return receiveBuilder()
		        .match(Request.class, message -> {
		        	businessService.perform(this + " " + (++count));
		        })
		        .match(Response.class, message -> {
		        	getSender().tell(count, getSelf());
		        })
		        .build();
	}
}
