package ru.bereshs.hhworksearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.mapper.EmployerMapper;
import ru.bereshs.hhworksearch.model.EmployerEntity;
import ru.bereshs.hhworksearch.openfeign.hhapi.EmployerFeignClient;
import ru.bereshs.hhworksearch.service.EmployerEntityService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployerClientService {
    private final EmployerEntityService service;
    private final EmployerMapper mapper;
    private final EmployerFeignClient client;

    public EmployerEntity getByHhId(String hhId) {
        return service.getByHhId(hhId).orElse(
                mapper.toEmployerEntity(client.getByHhId(hhId)));
    }

    public List<EmployerEntity> updateOnClient(List<EmployerEntity> list) {
        return list.stream()
                .peek(e -> mapper.updateEmployerEntity(e,
                        mapper.toEmployerEntity(client.getByHhId(e.getHhId()))))
                .toList();
    }

    public void saveAll(List<EmployerEntity> list) {
        list.forEach(e -> {
            Optional<EmployerEntity> optional = service.getByHhId(e.getHhId());
            if (optional.isEmpty()) {
                service.save(e);
            }
            if (optional.isPresent() && !optional.get().getName().equals(e.getName())) {
                EmployerEntity employer = optional.get();
                employer.setName(e.getName());
                service.save(employer);
            }
        });
    }
}
