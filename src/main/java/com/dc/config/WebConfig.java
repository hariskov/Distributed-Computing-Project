package com.dc.config;

import com.dc.interceptors.CardInterceptor;
import com.dc.interceptors.DeviceCheckerInterceptor;
import com.dc.interceptors.NewVoteInterceptor;
import com.dc.interceptors.VoteCalculatorInterceptor;
import com.dc.pojo.DeviceManager;
import com.dc.pojo.VotingManager;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

@Configuration
@ComponentScan({"com.dc"})
@EnableWebMvc
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
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);

        return mapper;
    }

    @Bean
    public VotingManager votingManager(){
        VotingManager vm = new VotingManager();
        return vm;
    }

    @Bean
    public DeviceManager devices(){
        DeviceManager deviceManager = new DeviceManager();
//        deviceManager.discoverDevices();
        return deviceManager;
    }

    @Bean
    public RestTemplate getRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // none for now , should add interceptor for calls to other servers
        // to verify whether they exist , add polling calls in here

        registry.addInterceptor(getDeviceCheckerInterceptor()).addPathPatterns("/echo/discovery");
        registry.addInterceptor(getNewVoteInterceptor()).addPathPatterns("/voting/startVote");
        registry.addInterceptor(getVoteCalculatorInterceptor()).addPathPatterns("/voting/getNetworkVotes");
        registry.addInterceptor(getCardInterceptor()).addPathPatterns("/card/playCard");
    }

    @Bean
    public VoteCalculatorInterceptor getVoteCalculatorInterceptor() {
        VoteCalculatorInterceptor voteCalculatorInterceptor = new VoteCalculatorInterceptor();
        return voteCalculatorInterceptor;
    }

    @Bean
    public CardInterceptor getCardInterceptor(){
        return new CardInterceptor();
    }

    @Bean
    public NewVoteInterceptor getNewVoteInterceptor(){return new NewVoteInterceptor();
    }

    @Bean
    public DeviceCheckerInterceptor getDeviceCheckerInterceptor() {
        return new DeviceCheckerInterceptor();
    }
}
