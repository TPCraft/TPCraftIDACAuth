## TPCraftIDAC Auth

TPCraft身份认证中心授权，适用于Minecraft服务器，玩家通过TPCraft身份认证中心登入到服务器。

## 版本兼容

全版本兼容

## 命令

| 命令           | 描述   | 权限         |
|--------------|------|------------|
| /auth        | 主命令  |            |
| /auth reload | 重载插件 | auth.admin |

## 配置文件

``` yml
webPort: 5900 # HTTP服务器端口

clientId: "" # OAuth2客户端标识
clientSecret: "" # OAuth2客户端密钥
redirectUri: "" # OAuth2客户端回调地址
```