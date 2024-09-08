package ru.bereshs.hhworksearch.service.impl;

import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.model.VacancyStatus;
import ru.bereshs.hhworksearch.model.dto.ReportDto;
import ru.bereshs.hhworksearch.service.DailyReportService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DailyReportServiceImpl implements DailyReportService {

    public ReportDto getReportDto(List<VacancyEntity> vacancyEntities) {
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
        List<String> experienceList = getExperienceList(vacancyEntities);
        List<SalaryList> salaryList = new ArrayList<>();
        experienceList.forEach(experience -> {
            int salary = getSalaryForExperience(vacancyEntities, experience);
            if (salary > 0) salaryList.add(new SalaryList(experience, salary));
        });
        return salaryList;
    }

    public List<String> getExperienceList(List<VacancyEntity> entities) {
        return entities.stream().map(VacancyEntity::getExperience).distinct().toList();
    }

    public int getSalaryForExperience(List<VacancyEntity> entities, String experience) {
        return (int) entities.stream().filter(entity -> entity.getExperience().equals(experience))
                .filter(entity -> entity.getSalary().getTo() > 0L)
                .mapToLong(entity -> entity.getSalary().getTo()).average().orElse(0D);
    }

    private long getLongOrNull(Map<VacancyStatus, Long> report, VacancyStatus status) {
        return report.get(status) != null ? report.get(status) : 0L;
    }

    private String getStringFromList(List<SalaryList> salary) {
        StringBuilder stringBuilder = new StringBuilder();
        salary.forEach(stringBuilder::append);
        return stringBuilder.toString();
    }

    public String getString(ReportDto reportDto) {
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
