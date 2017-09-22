package org.cynic.service;

import org.cynic.domain.WordStatistic;
import org.cynic.service.task.WordStatisticsInLinesRecursiveTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

@Service
public class CountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CountService.class);

    @Value("${count.task.batch.size}")
    private int maxConcurrentLines;

    @Value("${count.task.word.separator}")
    private String wordSeparator;

    public List<WordStatistic> countWordStatistics(List<String> lines) {
        LOGGER.info("countWordStatistics({})", lines);

        return new ForkJoinPool().invoke(new WordStatisticsInLinesRecursiveTask(lines, maxConcurrentLines, wordSeparator));
    }
}
