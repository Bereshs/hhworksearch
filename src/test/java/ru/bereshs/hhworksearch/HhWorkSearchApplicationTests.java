package ru.bereshs.hhworksearch;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.bereshs.hhworksearch.config.KafkaProducerConfig;
import ru.bereshs.hhworksearch.config.scheduler.GetVacanciesScheduler;
import ru.bereshs.hhworksearch.controller.web.AuthorizationController;
import ru.bereshs.hhworksearch.controller.ManagementController;
import ru.bereshs.hhworksearch.producer.KafkaProducerImpl;
import ru.bereshs.hhworksearch.service.impl.KeyEntityServiceImpl;

@SpringBootTest
class HhWorkSearchApplicationTests {


}
