package com.nevo.employeeservice.model.enums;

/**
 * @author Hamed Rostamzadeh
 */
public enum ErrorCodeEnum {
    OK(0),
    ERROR(1),
    MISSING_ARGUMENT(2);

    public final int value;

    ErrorCodeEnum(final int value) {
        this.value = value;
    }
}