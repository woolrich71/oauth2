package se.woolrich.mongo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.data.annotation.Id;


@JsonSerialize
public class TokenBlackList {

    @Id
    private String jti;
    private String userId;
    private Long expires;
    private Boolean isBlackListed;

    public TokenBlackList() {
    }

    public TokenBlackList(String userId, String jti, Long expires) {
        this.jti = jti;
        this.userId = userId;
        this.expires = expires;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getExpires() {
        return expires;
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }

    public boolean isBlackListed() {
        return isBlackListed;
    }

    public void setBlackListed(boolean blackListed) {
        isBlackListed = blackListed;
    }
}
