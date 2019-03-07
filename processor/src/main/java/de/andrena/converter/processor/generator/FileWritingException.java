/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.processor.generator;

import java.io.IOException;

class FileWritingException extends RuntimeException {
    FileWritingException(String message, IOException exception) {
        super(message, exception);
    }
}
