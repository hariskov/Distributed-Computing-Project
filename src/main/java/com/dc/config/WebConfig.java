package com.dc.config;

import com.dc.components.CustomRestTemplate;
import com.dc.interceptors.CardInterceptor;
import com.dc.interceptors.DeviceCheckerInterceptor;
import com.dc.interceptors.StartVoteInterceptor;
import com.dc.interceptors.NewVoteInterceptor;
import com.dc.pojo.DeviceManager;
import com.dc.pojo.VotingManager;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.codehaus.jackson.map.util.ISO8601DateFormat;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebMvc
@ImportResource("classpath:dc-servlet.xml")
@ComponentScan(basePackages = { "com.dc" },
        useDefaultFilters = false, includeFilters = @ComponentScan.Filter({Controller.class, Component.class}))
public class WebConfig extends WebMvcConfigurerAdapter{

    @Bean
    public UrlBasedViewResolver viewResolver(){
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        resolver.setSuffix(".jsp");
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setViewClass(JstlView.class);
        return resolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }

    @Bean
    public TilesViewResolver tilesViewResolver(){
        TilesViewResolver tilesViewResolver = new TilesViewResolver();
        tilesViewResolver.setViewClass(TilesView.class);

        return tilesViewResolver;
    }

    @Bean(name="restTemplate")
    public CustomRestTemplate restTemplate(){
        return new CustomRestTemplate();
    }

    @Bean
    public DeviceManager deviceManager(){
        DeviceManager deviceManager = new DeviceManager();
        return deviceManager;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry){
        registry.viewResolver(tilesViewResolver());
    }

    @Bean
    public TilesConfigurer tilesConfigurer(){
        TilesConfigurer tc = new TilesConfigurer();
        tc.setDefinitions(new String[]{"/WEB-INF/tiles.xml","/WEB-INF/views/views.xml"});
        tc.setCheckRefresh(true);
        return tc;
    }

    @Bean
    public VotingManager votingManager(){
        VotingManager vm = new VotingManager();
        return vm;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);

        return mapper;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // none for now , should add interceptor for calls to other servers
        // to verify whether they exist , add polling calls in here

        registry.addInterceptor(getDeviceCheckerInterceptor()).addPathPatterns("/echo/discovery");
        registry.addInterceptor(getStartVoteInterceptor()).addPathPatterns("/voting/startVote");
        registry.addInterceptor(getNewVoteInterceptor()).addPathPatterns("/voting/receiveNewTempVote");
        registry.addInterceptor(getCardInterceptor()).addPathPatterns("/card/playCard");
    }

    @Bean
    public NewVoteInterceptor getNewVoteInterceptor() {
        return new NewVoteInterceptor();
    }

    @Bean
    public CardInterceptor getCardInterceptor(){
        return new CardInterceptor();
    }

    @Bean
    public StartVoteInterceptor getStartVoteInterceptor(){return new StartVoteInterceptor();
    }

    @Bean
    public DeviceCheckerInterceptor getDeviceCheckerInterceptor() {
        return new DeviceCheckerInterceptor();
    }

    @Bean
    public ObjectMapper mapper(){
        Map<Class<?>,Class<?>> mix = new HashMap<Class<?>,Class<?>>();
        return new Jackson2ObjectMapperBuilder().featuresToDisable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)
                .dateFormat(new ISO8601DateFormat())
                .mixIns(mix)
                .build();
    }
}
