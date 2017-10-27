package com.dc.config;

import com.dc.components.CustomRestTemplate;
import com.dc.interceptors.*;
import com.dc.pojo.DeviceManager;
import com.dc.pojo.NewVotingManager;
import com.dc.pojo.VotingManager;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

@Configuration
@EnableWebMvc
@ImportResource("classpath:dc-servlet.xml")
@ComponentScan(basePackages = { "com.dc" },
        useDefaultFilters = false, includeFilters = @ComponentScan.Filter({Controller.class, Component.class}))
@EnableAsync
public class WebConfig extends WebMvcConfigurerAdapter{

    private HandlerInterceptor newSingleInterceptor;

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

    @Bean NewVotingManager newVotingManager(){
        NewVotingManager nvm = new NewVotingManager();
        return nvm;
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
        registry.addInterceptor(getNewVoteInterceptor()).addPathPatterns("/voting/receiveStage1Vote");
        registry.addInterceptor(getCardInterceptor()).addPathPatterns("/card/playCard");
        registry.addInterceptor(getReceiveVoteInterceptor()).addPathPatterns("/voting/receiveStage2Vote");
        registry.addInterceptor(getNewRoundInterceptor()).addPathPatterns("/game/applyPlayOrder");
    }

    @Bean
    public Stage1Interceptor getNewVoteInterceptor() {
        return new Stage1Interceptor();
    }

    @Bean
    public CardInterceptor getCardInterceptor(){
        return new CardInterceptor();
    }

    @Bean
    public StartVoteInterceptor getStartVoteInterceptor(){return new StartVoteInterceptor();}

    @Bean
    public NewRoundInterceptor getNewRoundInterceptor(){
        return new NewRoundInterceptor();
    }

    @Bean
    public TaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(50);
        return executor;
    }

    @Bean
    public DeviceCheckerInterceptor getDeviceCheckerInterceptor() {
        return new DeviceCheckerInterceptor();
    }

    @Bean
    public Stage2Interceptor getReceiveVoteInterceptor() {
        return new Stage2Interceptor();
    }

}
