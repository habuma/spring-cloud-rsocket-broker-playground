package habuma.service;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;

@Controller
@Slf4j
public class RefreshController {

	private DirectProcessor<RefreshEvent> hotSource = DirectProcessor.create();
	private Flux<RefreshEvent> refreshFlux;
	
	
	public RefreshController() {
		this.refreshFlux = hotSource.map(event -> event);
	}
	
	@MessageMapping("refreshing-service-rr")
	public Flux<RefreshEvent> refreshingServiceRSocketMessageHandler(String refreshingClient) {
		log.info("Received connection request: " + refreshingClient);
		return refreshFlux;
	}
	
	@PostMapping("/refresh")
	public @ResponseBody RefreshEvent refresh() {
		log.info("Publishing refresh event");
		RefreshEvent refreshEvent = new RefreshEvent("Refresh", System.currentTimeMillis());
		hotSource.onNext(refreshEvent);
		return refreshEvent;
	}
	
}
