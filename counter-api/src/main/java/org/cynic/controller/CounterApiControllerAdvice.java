package org.cynic.controller;

import org.cynic.exception.CounterApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CounterApiControllerAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(CounterApiControllerAdvice.class);

    private final MessageSourceAccessor messageSourceAccessor;

    @Autowired
    public CounterApiControllerAdvice(MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CounterApiException.class)
    public String businessException(CounterApiException exception) {
        log(exception);

        return messageSourceAccessor.getMessage(exception.getMessage(), exception.getValues());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Throwable.class)
    public String unknownException(Throwable exception) {
        log(exception);

        return messageSourceAccessor.getMessage("error.unknown", new Object[]{exception.getMessage()});
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String missingParameters(MissingServletRequestParameterException exception) {
        return messageSourceAccessor.getMessage("error.missing.parameter", new Object[]{exception.getParameterName()});
    }

    /**
     * Log exception
     *
     * @param exception exception
     */
    private void log(Throwable exception) {
        LOGGER.error("{}", exception);
    }
}
