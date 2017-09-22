package org.cynic.service;

import org.cynic.client.CounterServiceClient;
import org.cynic.domain.RangeStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

@Service
public class CountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CountService.class);

    private CounterServiceClient counterServiceClient;

    @Autowired
    public CountService(CounterServiceClient counterServiceClient) {
        this.counterServiceClient = counterServiceClient;
    }

    public List<RangeStatistics> calculateRangeStatistics(List<String> textFiles) {
        LOGGER.info("calculateRangeStatistics({})", textFiles);

        return textFiles.
                parallelStream().
                map(textFile -> counterServiceClient.countWords(Arrays.asList(textFile.split("\r\n")))).
                flatMap(Collection::stream).
                filter(wordStatistic -> wordStatistic.getWord().matches("(?i)^[A-Z].*$")).
                map(wordStatistic -> new RangeStatistics().
                        withLabel(wordToLabel(wordStatistic.getWord())).
                        withWord(wordStatistic.getWord(), wordStatistic.getCount())).
                reduce(new ArrayList<>(), accumulatingFunction(), combiningFunction());
    }

    private String wordToLabel(String word) {
        if (word.matches("(?i)^[A-G].*$")) {
            return "A-G";
        } else if (word.matches("(?i)^[H-N].*$")) {
            return "H-N";
        } else if (word.matches("(?i)^[O-U].*$")) {
            return "O-U";
        } else {
            return "V-Z";
        }
    }

    private BiFunction<List<RangeStatistics>, RangeStatistics, List<RangeStatistics>> accumulatingFunction() {
        return (rangeStatistics, rangeStatistic) -> {
            List<RangeStatistics> result = new ArrayList<>(rangeStatistics);
            addOrReplace(rangeStatistics, result, rangeStatistic);
            return result;
        };
    }

    private BinaryOperator<List<RangeStatistics>> combiningFunction() {
        return (rangeStatistics, otherRangeStatistics) -> {
            List<RangeStatistics> result = new ArrayList<>(rangeStatistics);
            if (!rangeStatistics.isEmpty()) {
                otherRangeStatistics.forEach(otherRangeStatistic -> {
                    addOrReplace(rangeStatistics, result, otherRangeStatistic);
                });
            } else {
                result.addAll(otherRangeStatistics);
            }

            return result;
        };
    }

    private void addOrReplace(List<RangeStatistics> rangeStatistics, List<RangeStatistics> result, RangeStatistics otherRangeStatistic) {
        if (rangeStatistics.contains(otherRangeStatistic)) {
            result.get(result.indexOf(otherRangeStatistic)).
                    withWords(otherRangeStatistic.getWords());
        } else {
            result.add(otherRangeStatistic);
        }
    }
}