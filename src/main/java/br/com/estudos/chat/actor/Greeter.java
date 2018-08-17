package br.com.estudos.chat.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import br.com.estudos.chat.actor.Printer.Greeting;

public class Greeter extends AbstractActor {
	private final String message;
	private final ActorRef printerActor;
	private String greeting = "";

	static public Props props(String message, ActorRef printerActor) {
		return Props.create(Greeter.class, () -> new Greeter(message, printerActor));
	}

	static public class WhoToGreet {
		public final String who;

		public WhoToGreet(String who) {
			this.who = who;
		}
	}

	static public class Greet {
		public Greet() {
		}
	}

	public Greeter(String message, ActorRef printerActor) {
		this.message = message;
		this.printerActor = printerActor;
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(WhoToGreet.class, wtg -> {
			this.greeting = message + ", " + wtg.who;
		}).match(Greet.class, x -> {
			printerActor.tell(new Greeting(greeting), getSelf());
		}).build();
	}

}
