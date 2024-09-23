package ru.bereshs.hhworksearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.mapper.EmployerMapper;
import ru.bereshs.hhworksearch.model.EmployerEntity;
import ru.bereshs.hhworksearch.openfeign.hhapi.EmployerFeignClient;
import ru.bereshs.hhworksearch.service.EmployerEntityService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployerClientService {
    private final EmployerEntityService service;
    private final EmployerMapper mapper;
    private final EmployerFeignClient client;

    public EmployerEntity getByHhId(String hhId) {
        return service.getByHhId(hhId);
    }

    public List<EmployerEntity> updateOnClient(List<EmployerEntity> list) {
        return list.stream()
                .peek(e -> mapper.updateEmployerEntity(e,
                        mapper.toEmployerEntity(client.getByHhId(e.getHhId()))))
                .toList();
    }

    public void saveAll(List<EmployerEntity> list) {
        service.saveAll(list);
    }
}
