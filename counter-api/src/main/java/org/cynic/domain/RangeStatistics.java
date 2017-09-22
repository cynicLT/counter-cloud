package org.cynic.domain;

import org.apache.commons.lang.ObjectUtils;

import java.math.BigInteger;
import java.util.*;

public class RangeStatistics implements Domain {
    private static final long serialVersionUID = -6884698873426074378L;

    private String label;

    private Map<String, BigInteger> words = new HashMap<>();

    public RangeStatistics withLabel(final String label) {
        this.label = label;
        return this;
    }

    public RangeStatistics withWord(String word, BigInteger count) {
        this.words.compute(word, (key, value) ->
                Optional.ofNullable(value).orElse(BigInteger.ZERO).add(count));
        return this;
    }

    public RangeStatistics withWords(Map<String, BigInteger> words) {
        words.forEach(this::withWord);
        return this;
    }

    public String getLabel() {
        return label;
    }

    public Map<String, BigInteger> getWords() {
        return words;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RangeStatistics that = (RangeStatistics) o;

        return label.equals(that.label);
    }

    @Override
    public int hashCode() {
        return label.hashCode();
    }


}

