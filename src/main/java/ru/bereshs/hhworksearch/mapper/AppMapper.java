package ru.bereshs.hhworksearch.mapper;

import ru.bereshs.hhworksearch.hhapiclient.dto.HhResumeDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhSalaryDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhSimpleListDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhVacancyDto;
import ru.bereshs.hhworksearch.model.*;
import ru.bereshs.hhworksearch.model.dto.SettingsDto;

import java.util.List;

public interface AppMapper {

    EmployerEntity toEmployerEntity(HhSimpleListDto listDto);

    ResumeEntity toResumeEntity(HhResumeDto resumeDto);

    VacancyEntity toVacancyEntity(HhVacancyDto vacancyDto);

    String toDateWithoutTimezone(String date);

    Integer toInt(HhSalaryDto salaryDto);
}
