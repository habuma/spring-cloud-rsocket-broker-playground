package habuma.client;

import org.springframework.context.ApplicationListener;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ClientEventListener implements ApplicationListener<PayloadApplicationEvent<RSocketRequester>>{

	@Override
	public void onApplicationEvent(PayloadApplicationEvent<RSocketRequester> event) {
		event.getPayload()
			.route("refreshing-service-rr")
			.data("Refreshing Client Connection Request")
			.retrieveFlux(RefreshEvent.class)
			.doOnNext(c -> {
				log.info("Refresh event received:  " + c);
			})
			.subscribe();
	}
		
}
