package ru.bereshs.hhworksearch.hhapiclient;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import ru.bereshs.hhworksearch.aop.Loggable;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhListDto;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public interface HeadHunterClient {
    public OAuth20Service getAuthService();

    public Response execute(Verb verb, String uri, OAuth2AccessToken token) throws IOException, ExecutionException, InterruptedException;

    public Response executeWithBody(Verb verb, String uri, OAuth2AccessToken token, Map<String, String> body) throws IOException, ExecutionException, InterruptedException;

    public HhListDto<HashMap<String, ?>> executeBody(Verb verb, String uri, OAuth2AccessToken token) throws IOException, ExecutionException, InterruptedException;

    public <T> T executeObject(Verb verb, String uri, OAuth2AccessToken token, Class<T> type) throws IOException, ExecutionException, InterruptedException;

    public OAuth2AccessToken requestAccessToken(String code) throws IOException, ExecutionException, InterruptedException;

    public OAuth2AccessToken requestRefreshToken(String refreshToken) throws IOException, ExecutionException, InterruptedException;

    public <T> HhListDto<T> getObjects(Verb verb, String uri, OAuth2AccessToken token, Class<T> type) throws IOException, ExecutionException, InterruptedException;

    public <T> HhListDto<T> getAllPagesObject(Verb verb, String uri, OAuth2AccessToken token, Class<T> type) throws IOException, ExecutionException, InterruptedException;

    public String addUriPageParameter(String uri, int page);

    public <T> T getHhObject(Object getMap, Class<T> type);

    public <T> T get(String url, Class<T> type);

    public <T> T post(String url, MultiValueMap<String, String> params, Class<T> type);

    public HttpHeaders postGetCookies(String url, MultiValueMap<String, String> params);
}
