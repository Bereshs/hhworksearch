package ru.bereshs.hhworksearch;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.bereshs.hhworksearch.config.KafkaProducerConfig;
import ru.bereshs.hhworksearch.config.SchedulerConfig;
import ru.bereshs.hhworksearch.controller.AuthorizationController;
import ru.bereshs.hhworksearch.controller.ManagementController;
import ru.bereshs.hhworksearch.hhapiclient.impl.HeadHunterClientRestTemplate;
import ru.bereshs.hhworksearch.producer.KafkaProducerImpl;
import ru.bereshs.hhworksearch.service.KeyEntityService;

@ActiveProfiles("Test")
@SpringBootTest
class HhWorkSearchApplicationTests {
	@MockBean
	KafkaProducerConfig kafkaProducerConfig;
	@MockBean
	SchedulerConfig schedulerConfig;
	@MockBean
	AuthorizationController authorizationController;
	@MockBean
    KeyEntityService keyEntityService;
	@MockBean
    HeadHunterClientRestTemplate headHunterClient;
	@MockBean
	KafkaProducerImpl kafkaProducer;
	@MockBean
	ManagementController managementController;
//its only for test
	@Test
	void contextLoads() {
	}

}
