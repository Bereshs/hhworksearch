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

import ru.bereshs.hhworksearch.exception.HhworkSearchTokenException;
import ru.bereshs.hhworksearch.mapper.VacancyMapper;
import ru.bereshs.hhworksearch.model.*;
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
    private final ParameterEntityService parameterService;
    private final VacancyMapper mapper;

    public HhResumeDto updateResume(ResumeEntity resume) throws IOException, ExecutionException, InterruptedException, HhworkSearchTokenException {
        if (resume.getNextPublish() == null || resume.getNextPublish().isBefore(LocalDateTime.now())) {
            postUpdateResume(resume.getHhId());
        }

        return getResumeById(resume.getHhId());
    }

    public List<VacancyEntity> getFullVacancyInformation(List<VacancyEntity> list) throws IOException, ExecutionException, InterruptedException, HhworkSearchTokenException {
        List<VacancyEntity> result = new ArrayList<>();
        for (VacancyEntity element : list) {
            var vacancyDto = getVacancyById(element.getHhId());
            result.add(mapper.toVacancyEntity(vacancyDto));
        }
        return result;
    }

    public OAuth2AccessToken getToken() throws HhworkSearchTokenException {
        try {
            KeyEntity key = keyEntityService.getByUserId(1L);

            if (keyEntityService.validateKey(key)) {
                return keyEntityService.getToken();
            }
            if (LocalDateTime.now().isBefore(key.getExpireIn()) && key.getRefreshToken() != null) {
                if (key.getRefreshToken().equals("refreshToken")) {
                    throw new HhworkSearchTokenException("Broken keyEntity");
                }
                OAuth2AccessToken token = getRefreshAccessToken(key.getRefreshToken());
                keyEntityService.saveToken(key, token);
                return key.getOAuth2AccessToken();
            }
            ParameterEntity clientId = parameterService.getByType(ParameterType.CLIENT_ID);
            OAuth2AccessToken token = getAccessToken(clientId.getData());
            keyEntityService.saveToken(key, token);
            return token;
        } catch (HhWorkSearchException | IOException | ExecutionException | InterruptedException ex) {

            throw new HhworkSearchTokenException("Token exception");
        }
    }


    public OAuth2AccessToken getRefreshAccessToken(String refreshToken) throws IOException, ExecutionException, InterruptedException {
        return headHunterClient.requestRefreshToken(refreshToken);
    }

    public OAuth2AccessToken getAccessToken(String code) throws IOException, ExecutionException, InterruptedException {
        return headHunterClient.requestAccessToken(code);
    }

    @Loggable
    public List<HhVacancyDto> getRecommendedVacancy(String key) throws IOException, ExecutionException, InterruptedException, HhworkSearchTokenException {
        HhListDto<HhVacancyDto> tempList = getPageRecommendedVacancy(0, key);
        List<HhVacancyDto> vacancyList = new ArrayList<>(tempList.getItems());
        for (int i = 1; i < tempList.getPages(); i++) {
            tempList = getPageRecommendedVacancy(i, key);
            vacancyList.addAll(tempList.getItems());
        }
        return vacancyList;
    }

    public HhVacancyDto getVacancyById(String vacancyId) throws IOException, ExecutionException, InterruptedException, HhworkSearchTokenException {
        OAuth2AccessToken token = getToken();
        String uri = appConfig.getVacancyConnectionString(vacancyId);
        return headHunterClient.executeObject(Verb.GET, uri, token, HhVacancyDto.class);
    }

    @Loggable
    public HhListDto<HhNegotiationsDto> getHhNegotiationsDtoList() throws IOException, ExecutionException, InterruptedException, HhworkSearchTokenException {
        OAuth2AccessToken token = getToken();
        String uri = appConfig.getNegotiationsConnectionString();
        return headHunterClient.getObjects(Verb.GET, uri, token, HhNegotiationsDto.class);

    }

    @Loggable
    public HhListDto<HhVacancyDto> getPageRecommendedVacancy(int page, String key) throws IOException, ExecutionException, InterruptedException, HhworkSearchTokenException {
        OAuth2AccessToken token = getToken();
        String uri = appConfig.getVacancyConnectionString(page, key);
        return headHunterClient.getObjects(Verb.GET, uri, token, HhVacancyDto.class);
    }

    @Loggable
    public HhListDto<HhVacancyDto> getPageRecommendedVacancyForResume(ResumeEntity resume) throws IOException, ExecutionException, InterruptedException, HhworkSearchTokenException {
        OAuth2AccessToken token = getToken();
        String uri = appConfig.getVacancyLikeResumeConnectionString(resume, 0);
        return headHunterClient.getObjects(Verb.GET, uri, token, HhVacancyDto.class);
    }

    @Loggable
    public HhListDto<HhResumeDto> getActiveResumes() throws IOException, ExecutionException, InterruptedException, HhworkSearchTokenException {
        OAuth2AccessToken token = getToken();
        String uri = appConfig.getResumesConnectionString();
        return headHunterClient.getObjects(Verb.GET, uri, token, HhResumeDto.class);
    }

    public HhResumeDto getResumeById(String resumeId) throws IOException, ExecutionException, InterruptedException, HhworkSearchTokenException {
        OAuth2AccessToken token = getToken();
        String uri = appConfig.getResumeByIdConnectrinString(resumeId);
        return headHunterClient.executeObject(Verb.GET, uri, token, HhResumeDto.class);
    }


    @Loggable
    public Response postNegotiation(HashMap<String, String> body) throws HhworkSearchTokenException {
        OAuth2AccessToken token = getToken();
        String uri = appConfig.getNegotiationPostConnetcionString();
        try (Response response = headHunterClient.executeWithBody(Verb.POST, uri, token, body)) {
            return response;
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException();
        }
    }


    @Loggable
    public HashMap<String, ?> getMePageBody() throws IOException, ExecutionException, InterruptedException, HhworkSearchTokenException {
        OAuth2AccessToken token = getToken();
        String uri = appConfig.getMeConnectionString();
        return stringToMap(headHunterClient.execute(Verb.GET, uri, token).getBody());

    }

    @Loggable
    public void postUpdateResume(String id) throws IOException, ExecutionException, InterruptedException, HhworkSearchTokenException {
        OAuth2AccessToken token = getToken();
        String uri = appConfig.getPostResume(id);
        var result = headHunterClient.execute(Verb.POST, uri, token);
    }

    public HashMap<String, ?> stringToMap(String body) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(body, HashMap.class);
    }
}
