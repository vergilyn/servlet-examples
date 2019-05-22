# tomcat-servlet-examples

遇到的问题:
```JAVA
    @Bean
    public TomcatWebServerFactoryCustomizer tomcatWebServerFactoryCustomizer(
            Environment environment, ServerProperties serverProperties) {
        return new TomcatWebServerFactoryCustomizer(environment, serverProperties){
            @Override
            public void customize(ConfigurableTomcatWebServerFactory factory) {
                super.customize(factory);
                factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
                    Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
                    protocol.setMaxConnections(0);
                    protocol.setMaxThreads(0);
                    protocol.setAcceptCount(0);
                });
            }
        };
    }
```
原意自定一config（不用application.yaml配置），但最后不生效（实质被默认值覆盖）！
参考源码: `org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory.customizeConnector()`
最后的`connector.protocolHandler -> Http11NioProtocol`中的类似`maxConnections | maxThreads | acceptCount`会被默认值覆盖。

特别：若application.yaml中配置成 0，也会被默认值覆盖。

`org.springframework.boot.autoconfigure.web.embedded.TomcatWebServerFactoryCustomizer.customize(...)`

### connectionTimeout
1. keepAliveTimeout == keepAliveTimeout == null ? connectionTimeout : keepAliveTimeout;
`org.apache.tomcat.util.net.AbstractEndpoint.getKeepAliveTimeout()`

2. 
When Tomcat expects data from the client, 
this is the time Tomcat will wait for that data to arrive before closing the connection.
当Tomcat希望从客户端获得数据时，这是Tomcat在关闭连接之前等待数据到达的时间。

`org.apache.coyote.AbstractProtocol.getConnectionTimeout()`

3.
NioSocketWrapper.setReadTimeout(getConnectionTimeout());
NioSocketWrapper.setWriteTimeout(getConnectionTimeout());

`org.apache.tomcat.util.net.NioEndpoint.Poller.register()`

4. 
`org.apache.tomcat.util.net.SecureNio2Channel.handshakeInternal()`