package ru.bereshs.hhworksearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.aop.Loggable;
import ru.bereshs.hhworksearch.mapper.VacancyRsMapper;
import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.model.VacancyStatus;
import ru.bereshs.hhworksearch.openfeign.hhapi.VacancyFeignClient;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.ListDto;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.NegotiationRs;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.PathParams;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.VacancyRs;
import ru.bereshs.hhworksearch.service.VacancyClientService;
import ru.bereshs.hhworksearch.service.VacancyEntityService;
import ru.bereshs.hhworksearch.service.VacancyFilterService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VacancyClientServiceImpl implements VacancyClientService {

    private final VacancyFeignClient client;
    private final VacancyEntityService service;
    private final VacancyRsMapper mapper;
    private final VacancyFilterService filter;


    public List<VacancyEntity>
    updateOnClient(List<VacancyEntity> list) {
        return list.stream().peek(e -> {
            if (e.getStatus().equals(VacancyStatus.FOUND)) {
                VacancyRs rs = client.getVacancyById(e.getHhId());
                mapper.updateVacancyEntity(e, rs);
            }
        }).filter(e -> e.getDescription() != null && e.getDescription().length() > 10).toList();
    }

    public Optional<VacancyEntity> getByHhId(String hhId) {
        return Optional.ofNullable(service.getByHhId(hhId).orElse(
                mapper.toVacancyEntity(client.getVacancyById(hhId))));
    }


    public VacancyRs getOnClientByHhId(String hhId) {
        return client.getVacancyById(hhId);
    }

    @Loggable
    public ListDto<VacancyRs> getPageVacancies(PathParams params) {
        return client.getVacancies(params);
    }

    public List<VacancyEntity> getAllPageVacancies(String key) {
        PathParams params = PathParams.builder()
                .period(1)
                .page(0)
                .per_page(100)
                .text(key)
                .searchFiled("name")
                .build();
        ListDto<VacancyRs> firstPage = getPageVacancies(params);
        List<VacancyEntity> result = new ArrayList<>(mapper.toVacancyEnityList(firstPage.items()));
        for (int i = 1; i < firstPage.pages(); i++) {
            params.setPage(i);
            result.addAll(
                    mapper.toListVacancyEntity(
                            getPageVacancies(params).items()
                    )
            );
        }
        return result;
    }

    @Loggable
    public ListDto<VacancyRs> getPageSimilarVacancies(String resumeId, PathParams params) {
        return client.getSimilarVacancies(resumeId, params);
    }

    public List<VacancyEntity> toVacancyEntity(List<VacancyRs> list) {
        return list.stream().map(mapper::toVacancyEntity).toList();
    }

    public List<VacancyEntity> getAllPageSimilarVacancies(String resumeId) {
        PathParams params = PathParams.builder()
                .period(1)
                .page(0)
                .per_page(100)
                .build();
        ListDto<VacancyRs> firstPage = getPageSimilarVacancies(resumeId, params);
        List<VacancyEntity> result = new ArrayList<>(mapper.toVacancyEnityList(firstPage.items()));
        for (int i = 1; i < firstPage.pages(); i++) {
            params.setPage(i);
            result.addAll(
                    mapper.toListVacancyEntity(
                            getPageSimilarVacancies(resumeId, params).items()
                    )
            );
        }
        return result;
    }

    @Loggable
    public void updateVacancyStatus(List<NegotiationRs> negotiationRsList) {
        List<VacancyEntity> listFromNegotiations = negotiationRsList.stream().map(mapper::toVacancyEntity).toList();
        service.updateVacancyStatusList(listFromNegotiations);
    }


    public List<VacancyEntity> filterList(List<VacancyEntity> list) {
        return list.stream().filter(filter::isSuitVacancy).toList();
    }

    @Loggable
    public void updateStatusVacancies(List<VacancyEntity> list, VacancyStatus status) {

        service.setStatusOnList(list, VacancyStatus.REQUEST);
        service.saveAll(list);
    }

    public void saveAll(List<VacancyEntity> list) {
        service.saveAll(list);
    }

    @Override
    @Loggable
    public List<VacancyEntity> getVacancyWithStatus(VacancyStatus vacancyStatus) {
        return service.getVacancyWithStatus(vacancyStatus);
    }

    public void updateStatusVacancy(VacancyEntity vacancy, VacancyStatus status) {
        Optional<VacancyEntity> o = service.getByHhId(vacancy.getHhId());
        if (o.isEmpty()) {
            return;
        }
        VacancyEntity v = o.get();
        v.setStatus(status);
        service.save(vacancy);
    }

    @Override
    public void save(VacancyEntity e) {
        service.save(e);
    }
}
