package org.example.yourownex.scheduler;

import org.example.yourownex.jooq.tables.records.*;
import org.example.yourownex.service.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class FulfillScheduler {

    private final FulfillService fulfillService;

    public FulfillScheduler(FulfillService fulfillService) {
        this.fulfillService = fulfillService;
    }

    // @Scheduled(fixedDelay = 0)
    public void fulfill() {
        List<FulfillmentRecord> records = fulfillService.getToFulfill();
        for (FulfillmentRecord record : records) {
            fulfillService.fulfill(record);
        }
    }
}
