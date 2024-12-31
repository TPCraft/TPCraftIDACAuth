# 警告
该项目已经弃坑，对于Minecraft Java服务器授权，我们已经使用YggdrasilAPI外置登录，详情请查看：[https://wiki.tpcraft.net/zh/TPCraftAPI/YggdrasilAPI](https://wiki.tpcraft.net/zh/TPCraftAPI/YggdrasilAPI)。

## TPCraftIDAC Auth

TPCraft身份认证中心授权，适用于Minecraft Java服务器，玩家通过TPCraft身份认证中心登入到服务器

## 相关文档

[https://tpcraft-1.gitbook.io/oauth2](https://tpcraft-1.gitbook.io/oauth2)

## 版本兼容

全版本兼容

## 功能

- 基础授权
- 自定义登入消息
- 自动登入
- 固定登入坐标
- 白名单
- (敬请期待)...

## 命令

| 命令                            | 描述         | 权限         |
|-------------------------------|------------|------------|
| /auth                         | 主命令        |            |
| /auth loginMessage on         | 启用登入消息     | auth.admin |
| /auth loginMessage off        | 禁用登入消息     | auth.admin |
| /auth autoLogin on            | 启用自动登入     | auth.admin |
| /auth autoLogin off           | 禁用自动登入     | auth.admin |
| /auth autoLogin set <超时时间(秒)> | 设置自动登入超时时间 | auth.admin |
| /auth loginPosition on        | 启用固定登入坐标   | auth.admin |
| /auth loginPosition off       | 禁用固定登入坐标   | auth.admin |
| /auth loginPosition set       | 设置固定登入坐标   | auth.admin |
| /auth whiteList on            | 启用白名单      | auth.admin |
| /auth whiteList off           | 禁用白名单      | auth.admin |
| /auth whiteList add <玩家名称>    | 添加白名单      | auth.admin |
| /auth whiteList remove <玩家名称> | 移除白名单      | auth.admin |
| /auth whiteList list          | 白名单列表      | auth.admin |
| /auth reload                  | 重载插件       | auth.admin |

## 配置文件

``` yml
########################################################################################
#   __________  ______           ______  ________  ___   ______   ___         __  __   #
#  /_  __/ __ \/ ____/________ _/ __/ /_/  _/ __ \/   | / ____/  /   | __  __/ /_/ /_  #
#   / / / /_/ / /   / ___/ __ `/ /_/ __// // / / / /| |/ /      / /| |/ / / / __/ __ \ #
#  / / / ____/ /___/ /  / /_/ / __/ /__/ // /_/ / ___ / /___   / ___ / /_/ / /_/ / / / #
# /_/ /_/    \____/_/   \__,_/_/  \__/___/_____/_/  |_\____/  /_/  |_\__,_/\__/_/ /_/  #
#                                                                                      #
########################################################################################

###################
#     注意事项     #
###################

# 创建 OAuth2 应用 https://auth.tpcraft.net/oauth2/client
# 如果域名未备案请使用IP作为回调地址

###################
#     配置文件     #
###################

# Web服务器端口
webPort: 5900

# OAuth2
oauth2:
  clientId: '' # 标识
  clientSecret: '' # 密钥
  redirectUri: '' # 回调地址

# 登入消息
# 变量 %player%(玩家名称)
loginMessage:
  enable: true # 是否启用
  join: '[&2+&r] &e%player%' # 进入消息
  leave: '[&4-&r] &e%player%' # 离开消息

# 自动登入
autoLogin:
  enable: false # 是否启用
  expires: 3600 # 超时时长(秒)

# 固定登入坐标
loginPosition:
  enable: false # 是否启用
  x: 0.0 # X轴
  y: 0.0 # Y轴
  z: 0.0 # Z轴

# 白名单
# 吐槽 原版白名单一点都不好用！
whiteList:
  enable: false # 是否启用
  allowPlayers: [ ] # 白名单列表
```
