package com.lostsidewalk.cultivator.api;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.ResponseEntity.ok;

/**
 * Controller class for handling API endpoints related to rules.
 *
 * This file is part of the Cultivator project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
@Slf4j
@RestController
public class RuleController {

    @Autowired
    KieContainer kieContainer;

    /**
     * Retrieves the list of rules.
     *
     * @return ResponseEntity containing the list of rules
     */
    @GetMapping("/rules")
    public ResponseEntity<List<String>> getRules() {
        log.info("Fetching rules");

        // Get the default KIE session
        KieSession kieSession = kieContainer.newKieSession();

        // Retrieve the KIE packages
        Iterable<KiePackage> kiePackages = kieSession.getKieBase().getKiePackages();

        // Iterate over the KIE packages and retrieve the rules
        List<String> rules = newArrayList();
        for (KiePackage kiePackage : kiePackages) {
            for (Rule rule : kiePackage.getRules()) {
                String ruleName = rule.getName();
                // Do something with the rule, e.g., print its name
                log.info("ruleName={}, packageName={}, loadOrder={}, metadata={}", rule.getName(), rule.getPackageName(), rule.getLoadOrder(), rule.getMetaData());
                rules.add(ruleName);
            }
        }

        // Dispose of the KIE session
        kieSession.dispose();
        return ok(rules);
    }
}
