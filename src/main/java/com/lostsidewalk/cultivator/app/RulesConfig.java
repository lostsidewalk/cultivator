package com.lostsidewalk.cultivator.app;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static org.kie.internal.io.ResourceFactory.newClassPathResource;

@Configuration
public class RulesConfig {

    private final KieServices kieServices = KieServices.Factory.get();

    private void buildKieRepository() {
        final KieRepository kieRepository = kieServices.getRepository();
        kieRepository.addKieModule(kieRepository::getDefaultReleaseId);
    }

    @Bean
    public KieContainer getKieContainer() {
        buildKieRepository();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        List<String> rules = List.of("com/lostsidewalk/cultivator/temperature-fan-rules.drl");
        for (String rule : rules) {
            kieFileSystem.write(newClassPathResource(rule));
        }
        KieBuilder kb = kieServices.newKieBuilder(kieFileSystem);
        kb.buildAll();
        KieModule kieModule = kb.getKieModule();

        return kieServices.newKieContainer(kieModule.getReleaseId());
    }
}
