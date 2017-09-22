package org.cynic.service.task;

import org.apache.commons.lang.StringUtils;
import org.cynic.domain.WordStatistic;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

/**
 * Fork-Join recursive task
 *
 * @see java.util.concurrent.RecursiveTask
 */
public class WordStatisticsInLinesRecursiveTask extends RecursiveTask<List<WordStatistic>> {
    private static final long serialVersionUID = -5124110641278276529L;
    private final List<String> lines;
    private final int countOfLinesThreshold;
    private final String wordSeparator;

    public WordStatisticsInLinesRecursiveTask(List<String> lines, int countOfLinesThreshold, String wordSeparator) {
        this.lines = lines;
        this.countOfLinesThreshold = countOfLinesThreshold;
        this.wordSeparator = wordSeparator;
    }

    @Override
    protected List<WordStatistic> compute() {
        if (lines.size() <= countOfLinesThreshold) {
            return lines.
                    stream().
                    filter(StringUtils::isNotEmpty).
                    map(this::countWordsInLine).
                    reduce(Collections.emptyList(), this::mergeWordStatistics);
        }

        RecursiveTask<List<WordStatistic>> recursiveTask = new WordStatisticsInLinesRecursiveTask(lines.subList(0, countOfLinesThreshold), countOfLinesThreshold, wordSeparator);
        recursiveTask.fork();

        return mergeWordStatistics(
                new WordStatisticsInLinesRecursiveTask(lines.subList(countOfLinesThreshold, lines.size()), countOfLinesThreshold, wordSeparator).compute(),
                recursiveTask.join()
        );
    }

    private List<WordStatistic> countWordsInLine(String line) {
        return
                Arrays.stream(StringUtils.split(line, wordSeparator)).
                        map(StringUtils::trim).
                        filter(StringUtils::isNotEmpty).
                        collect(Collectors.groupingBy(StringUtils::lowerCase, Collectors.counting())).
                        entrySet().
                        stream().
                        map(entry -> new WordStatistic().withWord(entry.getKey()).withCount(BigInteger.valueOf(entry.getValue()))).
                        collect(Collectors.toList());

    }

    private List<WordStatistic> mergeWordStatistics(List<WordStatistic> wordStatistics,
                                                    List<WordStatistic> otherWordStatistics) {
        List<WordStatistic> result = new ArrayList<>(wordStatistics);

        if (!result.isEmpty()) {
            otherWordStatistics.forEach(wordStatistic -> {
                if (result.contains(wordStatistic)) {
                    otherWordStatistics.stream().
                            filter(item -> item.equals(wordStatistic)).
                            forEach(item -> mergeStatistic(result, item));
                } else {
                    result.add(wordStatistic);
                }
            });
        } else {
            result.addAll(otherWordStatistics);
        }

        return result;
    }

    private void mergeStatistic(List<WordStatistic> result, WordStatistic item) {
        result.get(result.indexOf(item)).add(item.getCount());
    }

}
