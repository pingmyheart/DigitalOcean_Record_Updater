package com.russi.do_record_updater.util;

import java.net.SocketTimeoutException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ExceptionUtils {

    public AtomicReference<Boolean> isInSocketTimeoutException(Throwable throwable) {
        AtomicReference<Boolean> response;
        if (Objects.nonNull(throwable.getCause())) {
            if (throwable.getCause() instanceof SocketTimeoutException) {
                response = new AtomicReference<>(Boolean.TRUE);
                return response;
            } else {
                return isInSocketTimeoutException(throwable.getCause());
            }
        }
        response = new AtomicReference<>(Boolean.FALSE);
        return response;
    }
}
