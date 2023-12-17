package net.tpcraft.minecraft;

import java.util.List;

public class Config {
    private int webPort;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private Boolean coverInfo;
    private Boolean limitMode;
    private List<String> allowPlayers;

    public Config() {
    }

    public Config(int webPort, String clientId, String clientSecret, String redirectUri, Boolean coverInfo, Boolean limitMode, List<String> allowPlayers) {
        this.webPort = webPort;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.coverInfo = coverInfo;
        this.limitMode = limitMode;
        this.allowPlayers = allowPlayers;
    }

    public int getWebPort() {
        return webPort;
    }

    public void setWebPort(int webPort) {
        this.webPort = webPort;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public Boolean getCoverInfo() {
        return coverInfo;
    }

    public void setCoverInfo(Boolean coverInfo) {
        this.coverInfo = coverInfo;
    }

    public Boolean getLimitMode() {
        return limitMode;
    }

    public void setLimitMode(Boolean limitMode) {
        this.limitMode = limitMode;
    }

    public List<String> getAllowPlayers() {
        return allowPlayers;
    }

    public void setAllowPlayers(List<String> allowPlayers) {
        this.allowPlayers = allowPlayers;
    }
}
