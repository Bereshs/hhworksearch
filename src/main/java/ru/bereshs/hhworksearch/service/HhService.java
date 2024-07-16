package ru.bereshs.hhworksearch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import lombok.RequiredArgsConstructor;
import org.hibernate.persister.entity.EntityNameUse;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.aop.Loggable;
import ru.bereshs.hhworksearch.config.AppConfig;

import ru.bereshs.hhworksearch.model.KeyEntity;
import ru.bereshs.hhworksearch.model.ResumeEntity;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.hhapiclient.HeadHunterClient;
import ru.bereshs.hhworksearch.hhapiclient.dto.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;


@Service
@RequiredArgsConstructor
public class HhService {
    private final HeadHunterClient headHunterClient;
    private final AppConfig appConfig;
    private final KeyEntityService keyEntityService;

    public OAuth2AccessToken getToken() {
        try {
            return keyEntityService.getToken();
        } catch (HhWorkSearchException e) {
            KeyEntity key = keyEntityService.getByUserId(1L);
            if (key.getClientId() == null || !key.getClientId().equals(appConfig.getClientId()))
                key.setClientId(appConfig.getClientId());
            return getToken(key);
        }
    }

    public HhResumeDto updateResume(ResumeEntity resume) throws IOException, ExecutionException, InterruptedException {
        if (resume.getNextPublish() == null || resume.getNextPublish().isBefore(LocalDateTime.now())) {
            postUpdateResume(resume.getHhId());
        }

        return getResumeById(resume.getHhId());
    }

    public List<HhVacancyDto> getFullVacancyInformation(List<HhVacancyDto> list) throws IOException, ExecutionException, InterruptedException {
        List<HhVacancyDto> result = new ArrayList<>();
        for (HhVacancyDto element : list) {
            var vacancyDto = getVacancyById(element.getId());
            result.add(vacancyDto);
        }
        return result;
    }

    public OAuth2AccessToken getToken(KeyEntity key) {
        try {
            if (!key.isValid() && key.getRefreshToken() != null) {
                OAuth2AccessToken token = getRefreshToken(key.getRefreshToken());
                keyEntityService.saveToken(key, token);
                return token;
            }
            if (!key.isValid()) {
                OAuth2AccessToken token =  getAccessToken(key.getClientId());
                keyEntityService.saveToken(key, token);
                return token;
            }
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



    public OAuth2AccessToken getRefreshToken(String refreshToken) throws IOException, ExecutionException, InterruptedException {
        return headHunterClient.requestRefreshToken(refreshToken);
    }

    public OAuth2AccessToken getAccessToken(String code) throws IOException, ExecutionException, InterruptedException {
        return headHunterClient.requestAccessToken(code);
    }

    @Loggable
    public List<HhVacancyDto> getRecommendedVacancy(String key) throws IOException, ExecutionException, InterruptedException {
        HhListDto<HhVacancyDto> tempList = getPageRecommendedVacancy(0, key);
        List<HhVacancyDto> vacancyList = new ArrayList<>(tempList.getItems());
        for (int i = 1; i < tempList.getPages(); i++) {
            tempList = getPageRecommendedVacancy(i, key);
            vacancyList.addAll(tempList.getItems());
        }
        return vacancyList;
    }

    public HhVacancyDto getVacancyById(String vacancyId) throws IOException, ExecutionException, InterruptedException {
        OAuth2AccessToken token = getToken();
        String uri = appConfig.getVacancyConnectionString(vacancyId);
        return headHunterClient.executeObject(Verb.GET, uri, token, HhVacancyDto.class);
    }

    @Loggable
    public HhListDto<HhNegotiationsDto> getHhNegotiationsDtoList() throws IOException, ExecutionException, InterruptedException {
        OAuth2AccessToken token = getToken();
        String uri = appConfig.getNegotiationsConnectionString();
        return headHunterClient.getObjects(Verb.GET, uri, token, HhNegotiationsDto.class);

    }

    @Loggable
    public HhListDto<HhViewsResume> getHhViewsResumeDtoList(String resumeId) throws IOException, ExecutionException, InterruptedException {
        OAuth2AccessToken token = getToken();
        String uri = appConfig.getResumeViewsConnectionString(resumeId);
        return headHunterClient.getObjects(Verb.GET, uri, token, HhViewsResume.class);
    }

    @Loggable
    public HhListDto<HhVacancyDto> getPageRecommendedVacancy(int page, String key) throws IOException, ExecutionException, InterruptedException {
        OAuth2AccessToken token = getToken();
        String uri = appConfig.getVacancyConnectionString(page, key);
        return headHunterClient.getObjects(Verb.GET, uri, token, HhVacancyDto.class);
    }

    @Loggable
    public HhListDto<HhVacancyDto> getPageRecommendedVacancyForResume(ResumeEntity resume) throws IOException, ExecutionException, InterruptedException {
        OAuth2AccessToken token = getToken();
        String uri = appConfig.getVacancyLikeResumeConnectionString(resume, 0);
        return headHunterClient.getObjects(Verb.GET, uri, token, HhVacancyDto.class);
    }

    @Loggable
    public HhListDto<HhResumeDto> getActiveResumes() throws IOException, ExecutionException, InterruptedException {
        OAuth2AccessToken token = getToken();
        String uri = appConfig.getResumesConnectionString();
        return headHunterClient.getObjects(Verb.GET, uri, token, HhResumeDto.class);
    }

    public HhResumeDto getResumeById(String resumeId) throws IOException, ExecutionException, InterruptedException {
        OAuth2AccessToken token = getToken();
        String uri = appConfig.getResumeByIdConnectrinString(resumeId);
        return headHunterClient.executeObject(Verb.GET, uri, token, HhResumeDto.class);
    }

    /*@Loggable
    public <T> HhListDto<T> get(String connectionString,  Class<T> type) throws IOException, ExecutionException, InterruptedException {
        OAuth2AccessToken token = getToken();
        return headHunterClient.getObjects(Verb.GET, connectionString, token, type);
    }*/

    @Loggable
    public Response postNegotiation(HashMap<String, String> body) {
        OAuth2AccessToken token = getToken();
        String uri = appConfig.getNegotiationPostConnetcionString();
        try (Response response = headHunterClient.executeWithBody(Verb.POST, uri, token, body)) {
            return response;
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException();
        }
    }


    @Loggable
    public HashMap<String, ?> getMePageBody() throws IOException, ExecutionException, InterruptedException {
        OAuth2AccessToken token = getToken();
        String uri = appConfig.getMeConnectionString();
        return stringToMap(headHunterClient.execute(Verb.GET, uri, token).getBody());

    }

    @Loggable
    public void postUpdateResume(String id) throws IOException, ExecutionException, InterruptedException {
        OAuth2AccessToken token = getToken();
        String uri = appConfig.getPostResume(id);
        var result = headHunterClient.execute(Verb.POST, uri, token);
    }

    public HashMap<String, ?> stringToMap(String body) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(body, HashMap.class);
    }
}
