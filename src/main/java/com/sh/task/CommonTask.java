package com.sh.task;

import com.sh.remote.MexcApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommonTask {

    @Autowired
    private MexcApi mexcApi;

    @Scheduled(cron = "0/15 * * * * ?")
    public void execute() {
//        mexcApi.queryAllSymbols();
        mexcApi.queryDepth("BRIGHTID_USDT");
    }
}
