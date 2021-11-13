# KCache

这是一个多级缓存中间件，目的是为了整合进程内缓存和进程外缓存。比如说将高频访问的数据存储在进程内部。从而避免了网络开销。
进程内的缓存支持 [Caffine]
进程外缓存支持 [Redis] [Lettuce]

核心原理和思想来自于Alibaba的JetCache项目。
有对JetCache感兴趣的人，可以去查看JetCache的源代码。
本人开发这个项目目的仅仅只是***为了学习用途，请勿用作其他用途***。

## 基本模块

   * KCache-Core
        封装了对缓存操作的核心实现逻辑。
        提供了builder 函数来创建Cache。
        提供了最基础的CacheConfig来封装配置。
      
                

