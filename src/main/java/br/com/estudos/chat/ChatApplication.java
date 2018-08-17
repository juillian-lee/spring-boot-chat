package br.com.estudos.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
public class ChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class, args);
	}
	
	
//	@Bean
//	ActorSystem actorSystem() {
//		return ActorSystem.create("helloakka");
//	}
}
