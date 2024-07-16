package ru.bereshs.hhworksearch.model.dto;


import lombok.Setter;
import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.model.VacancyStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
public class DailyReportDto {

    private long requested;
    private long invited;
    private long discarded;
    private long founded;
    private int total;
    private List<SalaryList> salary;

    public DailyReportDto(List<VacancyEntity> vacancyEntities) {
        var report = vacancyEntities.stream().collect(Collectors.groupingBy(VacancyEntity::getStatus, Collectors.counting()));
        total = vacancyEntities.size();
        requested = getLongOrNull(report, VacancyStatus.REQUEST);
        invited = getLongOrNull(report, VacancyStatus.INVITATION)+getLongOrNull(report, VacancyStatus.invitation);
        discarded = getLongOrNull(report, VacancyStatus.DISCARD)+getLongOrNull(report, VacancyStatus.discard);
        founded = getLongOrNull(report, VacancyStatus.FOUND)+getLongOrNull(report, VacancyStatus.found);
        salary = getSalary(vacancyEntities);
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

    private String  getStringFromList(List<SalaryList> salary) {
        StringBuilder stringBuilder =  new StringBuilder();
        salary.forEach(stringBuilder::append);
        return stringBuilder.toString();
    }
    @Override
    public String toString() {
        return "Ежедневный отчет:\n" +
                "\tвсего записей " + total + "\n" +
                "\tотправлено запросов " + requested + "\n" +
                "\tприглашений " + invited + "\n" +
                "\tотказов " + discarded + "\n" +
                "\tне подошло " + founded + "\n" +
                "\tсредняя зарплата:\n " + getStringFromList(salary);
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
