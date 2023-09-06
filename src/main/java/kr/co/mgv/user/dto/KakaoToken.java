package kr.co.mgv.user.dto;

import lombok.Data;

@Data
public class KakaoToken {

    private String access_token;
    private String refresh_token;
    private String token_type;
    private String scope;
    private int expires_in;
    private int refresh_token_expires_in;

}
