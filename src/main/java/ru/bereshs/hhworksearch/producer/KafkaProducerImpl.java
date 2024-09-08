package ru.bereshs.hhworksearch.producer;

import com.github.scribejava.core.model.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.aop.Loggable;
import ru.bereshs.hhworksearch.config.AppConfig;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.model.ParameterType;
import ru.bereshs.hhworksearch.model.dto.TelegramMessageDto;
import ru.bereshs.hhworksearch.service.ParameterEntityService;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerImpl implements KafkaProducer {

    @Value("${spring.kafka.topic}")
    private String kafkaTopic;

    private final KafkaTemplate<Long, TelegramMessageDto> kafkaTemplate;
    private final ParameterEntityService parameterService;

    private final AppConfig appConfig;

    @Override
    @Loggable
    public void produce(TelegramMessageDto telegramMessageDto) {
        kafkaTemplate.send(kafkaTopic, telegramMessageDto);
    }

    public void produceDefault(String text) throws HhWorkSearchException {
        String telegramToken = parameterService.getByType(ParameterType.TELEGRAM_TOKEN).getData();
        String telegramChatId = parameterService.getByType(ParameterType.TELEGRAM_CHAT_ID).getData();

        TelegramMessageDto messageDto = new TelegramMessageDto(telegramToken, telegramChatId, text, LocalDateTime.now());
        produce(messageDto);
    }

    public void produce(Response response, String vacancyId) throws HhWorkSearchException {
        if (!response.isSuccessful()) {
            String text = "Необходимо участие vacancy Id: " + vacancyId + "\n" + "https://krasnodar.hh.ru/vacancy/" + vacancyId;
            produceDefault(text);
        }
    }
}
