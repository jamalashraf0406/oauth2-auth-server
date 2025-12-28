package com.yutube.oauth2.scheduler;

import com.yutube.oauth2.service.JwkKeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeyRotator {

    private final JwkKeyService jwkKeyService;

    /**
     * Rotate key every data every day
     * */
    @Scheduled(cron = "0 0 0 * * ?")
    public void rotate() {
        log.info("Scheduler is called to rotate the Auth Server key");
        jwkKeyService.rotateKey();
    }
}
