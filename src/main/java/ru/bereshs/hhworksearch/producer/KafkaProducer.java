package ru.bereshs.hhworksearch.producer;

import com.github.scribejava.core.model.Response;
import ru.bereshs.hhworksearch.model.dto.TelegramMessageDto;

public interface KafkaProducer {
    void produce(TelegramMessageDto telegramMessageDto);

    void produceDefault(String text);

    void produce(Response response, String vacancyId);
}
