package org.cynic.controller;

import org.apache.commons.io.IOUtils;
import org.cynic.exception.CounterApiException;
import org.cynic.service.CountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@RestController
public class CounterApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CounterApiController.class);

    private CountService countService;

    @Autowired
    public CounterApiController(CountService countService) {
        this.countService = countService;
    }


    @PostMapping(path = "/calculate-range-statistics")
    public @ResponseBody
    Callable<ResponseEntity<byte[]>> calculateRangeStatistics(@RequestParam("files") List<MultipartFile> files) {
        LOGGER.info("calculateRangeStatistics({})", files);

        return () ->
                ResponseEntity.ok().
                        contentType(MediaType.APPLICATION_OCTET_STREAM).
                        header("Content-Disposition", "attachment; filename=\"range-statistics.zip\"").
                        body(ZipHelper.zipRangeStatistics(
                                countService.calculateRangeStatistics(
                                        files.stream().map(file -> {
                                            try {
                                                return IOUtils.toString(file.getBytes(), Charset.defaultCharset().name());
                                            } catch (IOException e) {
                                                throw new CounterApiException("error.read.file", file.getName()).withCause(e);
                                            }
                                        }).collect(Collectors.toList()))));

    }
}
