package kr.co.mgv.user.service;

import kr.co.mgv.user.dto.KakaoToken;
import kr.co.mgv.user.dto.KakaoUserInfo;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.HashMap;

@Service
public class TokenService {

    private final WebClient webClient = WebClient.create();
    private static final String TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    private static final String REDIRECT_URI = "http://localhost/user/auth/kakao";
    private static final String USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String CLIENT_ID = "926865cb22da364155170d20476bc058";

    public KakaoToken getToken(String code) {
        String uri = TOKEN_URI + "?grant_type=" + GRANT_TYPE + "&client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&code=" + code;
        Flux<KakaoToken> response = webClient.post()
            .uri(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToFlux(KakaoToken.class);

        return response.blockFirst();
    }

    public KakaoUserInfo getUserInfo(String token) {
        Flux<KakaoUserInfo> response = webClient.get()
            .uri(USER_INFO_URI)
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .bodyToFlux(KakaoUserInfo.class);

        return response.blockFirst();
    }
}
