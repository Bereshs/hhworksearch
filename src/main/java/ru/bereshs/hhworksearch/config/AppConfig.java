package ru.bereshs.hhworksearch.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ru.bereshs.hhworksearch.model.ResumeEntity;

import static java.util.Objects.isNull;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
@PropertySource({
        "classpath:application.properties",
        "classpath:security.properties"
})

public class AppConfig {

    String hhApiUri;
    String emailEmail;
    String emailPassword;
    String hhVacancy;
    String hhResume;
    String hhApiCallback;
    String hhApiTokenUri;
    String hhPerPageParameter;
    String hhResumesPath;
    String clientId;
    String clientSecret;
    String telegramToken;
    String telegramChatId;
    String userAgent;
    String hhApiAuthorization;


    public String getVacancyConnectionString(Integer page, String key) {
        String uri = "https://api.hh.ru/vacancies?responses_count_enabled=true" +
                "&period=1" +
                "&order_by=publication_time" +
                "&vacancy_search_fields=name" +
                "&text=" + key + hhPerPageParameter;
        if (!isNull(page) && page > 0) {
            uri += "&page=" + page;
        }
        return uri;
    }

    public String getVacancyLikeResumeConnectionString(ResumeEntity resume, int page) {
        String uri = hhResumesPath + resume.getHhId() + "/similar_vacancies" +
                "?period=1" + hhPerPageParameter;
        if (!isNull(page) && page > 0) {
            uri += "&page=" + page;
        }
        return uri;
    }

    public String getNegotiationsConnectionString() {

        return "https://api.hh.ru/negotiations?" +
                "order_by=updated_at" + hhPerPageParameter;
    }


    public String getResumeViewsConnectionString(String resumeId) {
        return hhResumesPath + resumeId + "/views";
    }


    public String getResumesConnectionString() {
        return hhResumesPath+"mine";
    }

    public String getResumeByIdConnectrinString(String resumeId) {
        return hhResumesPath + resumeId;
    }

    public String getPostResume(String resumeId) {
        return hhResumesPath + resumeId + "/publish";
    }

    public String getVacancyConnectionString(String id) {
        return "https://api.hh.ru/vacancies/" + id;
    }

    public String getNegotiationPostConnetcionString() {
        return "https://api.hh.ru/negotiations";
    }
    public String getMeConnectionString() {return "https://api.hh.ru/me";}
    public String getAuthorizationConnectionString() {
        return getHhApiAuthorization()+"?response_type=code&"
                +"client_id="+getClientId();
    }
}
