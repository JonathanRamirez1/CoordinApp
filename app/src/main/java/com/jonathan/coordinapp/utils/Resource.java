package com.jonathan.coordinapp.utils;

public class Resource<T> {
    public enum Status {LOADING, SUCCESS, ERROR}

    public final Status status;
    public final T data;
    public final Throwable error;

    private Resource(Status s, T d, Throwable e) {
        status = s;
        data = d;
        error = e;
    }

    public static <T> Resource<T> loading() {
        return new Resource<>(Status.LOADING, null, null);
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(Throwable e) {
        return new Resource<>(Status.ERROR, null, e);
    }
}
