package ru.bereshs.hhworksearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.aop.Loggable;
import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.model.VacancyStatus;
import ru.bereshs.hhworksearch.model.dto.ReportDto;
import ru.bereshs.hhworksearch.service.DailyReportService;
import ru.bereshs.hhworksearch.service.VacancyEntityService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyReportServiceImpl implements DailyReportService {

    private final VacancyEntityService vacancyEntityService;

    @Loggable
    public String getDaily() {
        var vacancyEntities = vacancyEntityService.getVacancyEntityByTimeStampAfter(LocalDateTime.now().minusDays(1));
        ReportDto reportDto = getReportDto(vacancyEntities);
        return getString(reportDto);
    }

    public ReportDto getReportDto(List<VacancyEntity> vacancyEntities) {
        if (vacancyEntities == null || vacancyEntities.isEmpty()) {
            return new ReportDto(0L,0L,0L,0L,0L,"");
        }

        var report = vacancyEntities.stream().collect(Collectors.groupingBy(VacancyEntity::getStatus, Collectors.counting()));
        Long total = (long) vacancyEntities.size();
        Long requested = getLongOrNull(report, VacancyStatus.REQUEST);
        Long invited = getLongOrNull(report, VacancyStatus.INVITATION) + getLongOrNull(report, VacancyStatus.invitation);
        Long discarded = getLongOrNull(report, VacancyStatus.DISCARD) + getLongOrNull(report, VacancyStatus.discard);
        Long founded = getLongOrNull(report, VacancyStatus.FOUND) + getLongOrNull(report, VacancyStatus.found);
        String salary = getStringFromList(getSalary(vacancyEntities));

        return new ReportDto(requested, invited, discarded, founded, total, salary);
    }

    public List<SalaryList> getSalary(List<VacancyEntity> vacancyEntities) {
        if (vacancyEntities == null || vacancyEntities.isEmpty()) {
            return null;
        }

        List<String> experienceList = getExperienceList(vacancyEntities);
        List<SalaryList> salaryList = new ArrayList<>();
        experienceList.forEach(experience -> {
            int salary = getSalaryForExperience(vacancyEntities, experience);
            if (salary > 0) salaryList.add(new SalaryList(experience, salary));
        });
        return salaryList;
    }

    public List<String> getExperienceList(List<VacancyEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return null;
        }
        return entities.stream().map(VacancyEntity::getExperience).distinct().toList();
    }

    public int getSalaryForExperience(List<VacancyEntity> entities, String experience) {
        if (entities == null || experience == null || entities.isEmpty()) {
            return 0;
        }
        return (int) entities.stream().filter(entity -> entity.getExperience() != null && entity.getExperience().equals(experience))
                .filter(entity -> entity.getSalary() != null && entity.getSalary() > 0L)
                .mapToLong(VacancyEntity::getSalary).average().orElse(0D);
    }

    private long getLongOrNull(Map<VacancyStatus, Long> report, VacancyStatus status) {
        if (report == null || report.isEmpty() || status == null) {
            return 0L;
        }

        return report.get(status) != null ? report.get(status) : 0L;
    }

    private String getStringFromList(List<SalaryList> salary) {
        if (salary == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        salary.forEach(stringBuilder::append);
        return stringBuilder.toString();
    }

    public String getString(ReportDto reportDto) {
        if (reportDto == null) {
            return null;
        }
        return "Ежедневный отчет:\n" +
                "\tвсего записей " + reportDto.total() + "\n" +
                "\tотправлено запросов " + reportDto.requested() + "\n" +
                "\tприглашений " + reportDto.invited() + "\n" +
                "\tотказов " + reportDto.discarded() + "\n" +
                "\tне подошло " + reportDto.founded() + "\n" +
                "\tсредняя зарплата:\n " + reportDto.salary();
    }

    public static class SalaryList {
        private final String experience;
        private final int salary;

        public SalaryList(String experience, int salary) {
            this.experience = experience;
            this.salary = salary;
        }

        @Override
        public String toString() {
            return "\t\t" + experience + ": " + salary + "\n";
        }
    }

}
