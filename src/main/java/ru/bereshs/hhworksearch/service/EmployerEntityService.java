package ru.bereshs.hhworksearch.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.mapper.AppMapper;
import ru.bereshs.hhworksearch.model.EmployerEntity;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhVacancyDto;
import ru.bereshs.hhworksearch.repository.EmployerEntityRepository;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhSimpleListDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployerEntityService {

    private final EmployerEntityRepository employerEntityRepository;
    private final AppMapper mapper;


    public EmployerEntity getByHhId(String hhId) {
        return employerEntityRepository.getByHhId(hhId);
    }

    EmployerEntity getByEmployerDto(HhSimpleListDto employerDto) {
        EmployerEntity employer = employerEntityRepository.getByHhId(String.valueOf(employerDto.getId()));
        if (employer == null) {
            employer = new EmployerEntity();
            employer.setHhId(String.valueOf(employerDto.getId()));
            employer.setName(employerDto.getName());
            employer.setUrl(employer.getUrl());
            employerEntityRepository.save(employer);
        }

        return employer;
    }

    public List<EmployerEntity> extractEmployers(List<HhVacancyDto> list) {
        return list.stream().map(entity -> mapper.toEmployerEntity(entity.getEmployer())).toList();
    }

    public void saveAll(List<EmployerEntity> list) {
        list.stream().filter(employer -> !employerEntityRepository.existsByHhId(employer.getHhId()))
                .forEach(employerEntityRepository::save);
    }


}
