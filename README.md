## TPCraftIDACAuth

TPCraft身份认证中心授权，适用于Minecraft服务器，玩家通过TPCraft身份认证中心登入到服务器。

## 版本兼容

全版本兼容

## 命令

| 命令                      | 描述           | 权限         |
|-------------------------|--------------|------------|
| /auth                   | 主命令          |            |
| /auth coverInfo on      | 启用进入/离开服务器消息 | auth.admin |
| /auth coverInfo off     | 禁用进入/离开服务器消息 | auth.admin |
| /auth limit on          | 启用限制模式       | auth.admin |
| /auth limit off         | 禁用限制模式       | auth.admin |
| /auth limit add [名称]    | 添加限制模式白名单    | auth.admin |
| /auth limit remove [名称] | 移除限制模式白名单    | auth.admin |
| /auth limit list        | 限制模式白名单列表    | auth.admin |
| /auth reload            | 重载插件         | auth.admin |

## 配置文件

``` yml
# Web服务器端口
webPort: 5900

# 前往 https://auth.tpcraft.net/oauth2/client 创建 OAuth2 客户端
# OAuth2客户端配置
clientId: ''
clientSecret: ''
redirectUri: ''

# 进入/离开服务器消息
coverInfo: true

# 限制模式
limitMode: false
# 限制模式白名单
allowPlayers:
  - ''
```