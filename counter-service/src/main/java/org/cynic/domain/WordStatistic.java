package org.cynic.domain;

import java.math.BigInteger;

public class WordStatistic implements Domain {
    private static final long serialVersionUID = 6072594410990666840L;

    private String word;

    private BigInteger count;

    public WordStatistic withWord(final String word) {
        this.word = word;
        return this;
    }

    public WordStatistic withCount(final BigInteger count) {
        this.count = count;
        return this;
    }

    public void add(BigInteger otherCount) {
        this.count = this.count.add(otherCount);
    }

    public BigInteger getCount() {
        return count;
    }

    public String getWord() {
        return word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WordStatistic that = (WordStatistic) o;

        return word.equals(that.word);
    }

    @Override
    public int hashCode() {
        return word.hashCode();
    }
}

