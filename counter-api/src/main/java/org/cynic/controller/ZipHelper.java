package org.cynic.controller;

import org.apache.commons.lang.StringUtils;
import org.cynic.domain.RangeStatistics;
import org.cynic.exception.CounterApiException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class ZipHelper {

    private ZipHelper() {
    }


    public static byte[] zipRangeStatistics(List<RangeStatistics> rangeStatistics) {
        try {
            try (ByteArrayOutputStream result = new ByteArrayOutputStream();
                 ZipOutputStream zipOutputStream = new ZipOutputStream(result);) {

                rangeStatistics.
                        stream().
                        map(rangeStatistic -> new AbstractMap.SimpleEntry<>(rangeStatistic.getLabel(), toBytes(rangeStatistic))).
                        forEach(entry -> {
                            try {
                                zipOutputStream.putNextEntry(new ZipEntry(entry.getKey()));
                                zipOutputStream.write(entry.getValue());
                                zipOutputStream.closeEntry();
                            } catch (IOException e) {
                                throw new CounterApiException("error.create.zip").withCause(e);
                            }
                        });
                return result.toByteArray();
            }
        } catch (IOException e) {
            throw new CounterApiException("error.create.zip").withCause(e);
        }
    }

    private static byte[] toBytes(RangeStatistics rangeStatistic) {
        return org.apache.tomcat.util.codec.binary.StringUtils.getBytesUtf8(
                rangeStatistic.
                        getWords().
                        entrySet().
                        parallelStream().
                        map(entry -> StringUtils.join(new String[]{entry.getKey(), String.valueOf(entry.getValue())}, " - ")).
                        collect(Collectors.joining("\n"))
        );
    }
}
