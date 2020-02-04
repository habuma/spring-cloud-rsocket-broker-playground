package habuma.client;

import org.springframework.cloud.gateway.rsocket.client.BrokerClient;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientEventListener {

	private final BrokerClient brokerClient;

	@EventListener
	public void getClient(PayloadApplicationEvent<RSocketRequester> event) {
		event.getPayload()
			.route("refreshing-service-rr")
			.metadata(brokerClient.forwarding(builder -> 
					builder.serviceName("refreshing-service")
						.with("multicast", "true")))
			.data("Refreshing Client Connection Request")
			.retrieveFlux(RefreshEvent.class)
			.doOnNext(c -> {
				log.info("Refresh event received:  " + c);
			})
			.subscribe();
	}
		
}
