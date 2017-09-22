package org.cynic.client;

import org.cynic.domain.WordStatistic;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(serviceId = "counter-service")
public interface CounterServiceClient {
    @PostMapping(path = "count-words")
    @ResponseBody
    List<WordStatistic> countWords(@RequestBody List<String> lines);
}
