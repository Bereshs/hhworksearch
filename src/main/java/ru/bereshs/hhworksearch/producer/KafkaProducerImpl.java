package ru.bereshs.hhworksearch.producer;

import com.github.scribejava.core.model.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.aop.Loggable;
import ru.bereshs.hhworksearch.config.AppConfig;
import ru.bereshs.hhworksearch.model.dto.TelegramMessageDto;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerImpl implements KafkaProducer {

    @Value("${spring.kafka.topic}")
    private String kafkaTopic;

    private final KafkaTemplate<Long, TelegramMessageDto> kafkaTemplate;

    private final AppConfig appConfig;

    @Override
    @Loggable
    public void produce(TelegramMessageDto telegramMessageDto) {
        kafkaTemplate.send(kafkaTopic, telegramMessageDto);
    }

    public void produceDefault(String text) {
        TelegramMessageDto messageDto = new TelegramMessageDto(appConfig.getTelegramToken(), appConfig.getTelegramChatId(), text, LocalDateTime.now());
        produce(messageDto);
    }

    public void produce(Response response, String vacancyId) {
        if (!response.isSuccessful()) {
            String text = "Необходимо участие vacancy Id: " + vacancyId + "\n" + "https://krasnodar.hh.ru/vacancy/" + vacancyId;
            produceDefault(text);
        }
    }
}
