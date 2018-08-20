package br.com.estudos.chat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import br.com.estudos.chat.actor.WorkerActor;
import br.com.estudos.chat.component.ActorFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

@Component
public class Runner implements CommandLineRunner {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ActorSystem actorSystem;

	@Autowired
	ActorFactory actorFactory;
	
	@Override
	public void run(String... args) throws Exception {
		String sentence;
		String modifiedSentence;
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		Socket clientSocket = new Socket("localhost", 9091);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		sentence = inFromUser.readLine();
		outToServer.writeBytes(sentence + 'n');
		modifiedSentence = inFromServer.readLine();
		System.out.println("FROM SERVER: " + modifiedSentence);
		clientSocket.close();
//		try {
//            ActorRef workerActor = actorFactory.getActorRef(WorkerActor.class, "worker-actor");
//
//            workerActor.tell(new WorkerActor.Request(), null);
//            workerActor.tell(new WorkerActor.Request(), null);
//            workerActor.tell(new WorkerActor.Request(), null);
//
//            FiniteDuration duration = FiniteDuration.create(1, TimeUnit.SECONDS);
//            Future<Object> awaitable = Patterns.ask(workerActor, new WorkerActor.Response(), Timeout.durationToTimeout(duration));
//
//            logger.info("Response: " + Await.result(awaitable, duration));
//            
//            workerActor = actorFactory.getActorRef(WorkerActor.class, "worker-actor");
//            workerActor.tell(new WorkerActor.Request(), null);
//            workerActor.tell(new WorkerActor.Request(), null);
//            workerActor.tell(new WorkerActor.Request(), null);
//
//            duration = FiniteDuration.create(1, TimeUnit.SECONDS);
//            awaitable = Patterns.ask(workerActor, new WorkerActor.Response(), Timeout.durationToTimeout(duration));
//
//            logger.info("Response: " + Await.result(awaitable, duration));
//            
//        } finally {
//            actorSystem.terminate();
//            Await.result(actorSystem.whenTerminated(), Duration.Inf());
//        }
	}
}
