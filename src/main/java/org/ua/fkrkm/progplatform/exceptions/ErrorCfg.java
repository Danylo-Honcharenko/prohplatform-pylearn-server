package org.ua.fkrkm.progplatform.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorCfg {
    private Map<String, String> messages;
}
