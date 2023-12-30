package net.tpcraft.minecraft;

import java.util.List;

public class Config {
    private Integer webPort;
    private String oauth2ClientId;
    private String oauth2ClientSecret;
    private String oauth2RedirectUri;
    private Boolean loginMessageEnable;
    private String loginMessageJoin;
    private String loginMessageLeave;
    private Boolean autoLoginEnable;
    private Integer autoLoginExpires;
    private Boolean loginPositionEnable;
    private Double loginPositionX;
    private Double loginPositionY;
    private Double loginPositionZ;
    private Boolean whiteListEnable;
    private List<String> whiteListAllowPlayers;

    public Config() {
    }

    public Integer getWebPort() {
        return webPort;
    }

    public void setWebPort(Integer webPort) {
        this.webPort = webPort;
    }

    public String getOauth2ClientId() {
        return oauth2ClientId;
    }

    public void setOauth2ClientId(String oauth2ClientId) {
        this.oauth2ClientId = oauth2ClientId;
    }

    public String getOauth2ClientSecret() {
        return oauth2ClientSecret;
    }

    public void setOauth2ClientSecret(String oauth2ClientSecret) {
        this.oauth2ClientSecret = oauth2ClientSecret;
    }

    public String getOauth2RedirectUri() {
        return oauth2RedirectUri;
    }

    public void setOauth2RedirectUri(String oauth2RedirectUri) {
        this.oauth2RedirectUri = oauth2RedirectUri;
    }

    public Boolean getLoginMessageEnable() {
        return loginMessageEnable;
    }

    public void setLoginMessageEnable(Boolean loginMessageEnable) {
        this.loginMessageEnable = loginMessageEnable;
    }

    public String getLoginMessageJoin() {
        return loginMessageJoin;
    }

    public void setLoginMessageJoin(String loginMessageJoin) {
        this.loginMessageJoin = loginMessageJoin;
    }

    public String getLoginMessageLeave() {
        return loginMessageLeave;
    }

    public void setLoginMessageLeave(String loginMessageLeave) {
        this.loginMessageLeave = loginMessageLeave;
    }

    public Boolean getAutoLoginEnable() {
        return autoLoginEnable;
    }

    public void setAutoLoginEnable(Boolean autoLoginEnable) {
        this.autoLoginEnable = autoLoginEnable;
    }

    public Integer getAutoLoginExpires() {
        return autoLoginExpires;
    }

    public void setAutoLoginExpires(Integer autoLoginExpires) {
        this.autoLoginExpires = autoLoginExpires;
    }

    public Boolean getLoginPositionEnable() {
        return loginPositionEnable;
    }

    public void setLoginPositionEnable(Boolean loginPositionEnable) {
        this.loginPositionEnable = loginPositionEnable;
    }

    public Double getLoginPositionX() {
        return loginPositionX;
    }

    public void setLoginPositionX(Double loginPositionX) {
        this.loginPositionX = loginPositionX;
    }

    public Double getLoginPositionY() {
        return loginPositionY;
    }

    public void setLoginPositionY(Double loginPositionY) {
        this.loginPositionY = loginPositionY;
    }

    public Double getLoginPositionZ() {
        return loginPositionZ;
    }

    public void setLoginPositionZ(Double loginPositionZ) {
        this.loginPositionZ = loginPositionZ;
    }

    public Boolean getWhiteListEnable() {
        return whiteListEnable;
    }

    public void setWhiteListEnable(Boolean whiteListEnable) {
        this.whiteListEnable = whiteListEnable;
    }

    public List<String> getWhiteListAllowPlayers() {
        return whiteListAllowPlayers;
    }

    public void setWhiteListAllowPlayers(List<String> whiteListAllowPlayers) {
        this.whiteListAllowPlayers = whiteListAllowPlayers;
    }
}
