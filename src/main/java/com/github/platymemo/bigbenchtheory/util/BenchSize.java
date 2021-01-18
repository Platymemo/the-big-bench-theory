package com.github.platymemo.bigbenchtheory.util;

public enum BenchSize {
    TINY(1),
    BIG(5),
    BIGGER(7),
    BIGGEST(9);

    public final int size;

    BenchSize(int size) {
        this.size = size;
    }
}
