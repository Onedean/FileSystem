package com.cw.filesystem.exception;


//异常处理类
public class FileSystemException extends RuntimeException {

    public FileSystemException() {
        super();
    }

    public FileSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileSystemException(String message) {
        super(message);
    }

    public FileSystemException(Throwable cause) {
        super(cause);
    }

}
