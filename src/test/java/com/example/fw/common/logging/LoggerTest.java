package com.example.fw.common.logging;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class LoggerTest {
    // TODO: 仮のテストコード

    @Test
    void testApplicationLogger() {
        ApplicationLogger appLogger = LoggerFactory.getApplicationLogger(log);
        appLogger.debug("Debugログ:{0}", "test1");
        appLogger.info("i.ex.fw.0001", "test2");
        appLogger.warn("w.ex.fw.0001", "test3");
    }

    @Test
    void testMonitoringLogger() {
        MonitoringLogger monitoringLogger = LoggerFactory.getMonitoringLogger(log);
        monitoringLogger.error("e.ex.fw.0001", "test");
    }

    @Test
    void testAuditLogger() {
        AuditLogger auditLogger = LoggerFactory.getAuditLogger(log);
        auditLogger.audit("i.ex.fw.0001", "test");
    }

}
