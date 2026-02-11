package com.kkarimi.eventmanagement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;
import org.springframework.modulith.core.ApplicationModules;

class ModularityTest {

    @Test
    @EnabledForJreRange(max = JRE.JAVA_24)
    void verifiesModuleStructure() {
        ApplicationModules.of(EventManagementApplication.class).verify();
    }
}
