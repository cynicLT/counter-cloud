package org.cynic.controller;

import org.cynic.domain.WordStatistic;
import org.cynic.service.CountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Callable;

@Controller
public class CounterController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CounterController.class);

    private final CountService countService;

    @Autowired
    public CounterController(CountService countService) {
        this.countService = countService;
    }

    @PostMapping(path = "count-words")
    public @ResponseBody
    Callable<List<WordStatistic>> countWords(@RequestBody List<String> lines) {
        LOGGER.info("countWords({})", lines);

        return () -> countService.countWordStatistics(lines);
    }
}
