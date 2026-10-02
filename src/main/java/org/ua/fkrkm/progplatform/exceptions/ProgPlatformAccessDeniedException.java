package org.ua.fkrkm.progplatform.exceptions;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.AccessDeniedException;

public class ProgPlatformAccessDeniedException extends AccessDeniedException {
    public ProgPlatformAccessDeniedException(ErrorCfg errorCfg) {
        super(errorCfg.getMessages().get(LocaleContextHolder.getLocale().getLanguage()));
    }
    public ProgPlatformAccessDeniedException(String message) {
        super(message);
    }
}
