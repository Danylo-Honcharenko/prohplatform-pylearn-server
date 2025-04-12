package org.ua.fkrkm.progplatform.exceptions;

import org.springframework.context.i18n.LocaleContextHolder;

public class ProgPlatformException extends RuntimeException {
    public ProgPlatformException(ErrorCfg errorCfg) {
        super(errorCfg.getMessages().get(LocaleContextHolder.getLocale().getLanguage()));
    }
    public ProgPlatformException(String message) {
        super(message);
    }
    public ProgPlatformException() {
        super();
    }
}
