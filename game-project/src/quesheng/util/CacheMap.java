/*
 * Decompiled with CFR 0.152.
 */
package quesheng.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class CacheMap
extends LinkedHashMap {
    private int MAX_ENTRIES = 100;

    public CacheMap(int MAX_ENTRIES, int initialCapacity) {
        super(initialCapacity);
        this.MAX_ENTRIES = MAX_ENTRIES;
    }

    public CacheMap(int MAX_ENTRIES) {
        this.MAX_ENTRIES = MAX_ENTRIES;
    }

    protected boolean removeEldestEntry(Map.Entry eldest) {
        return this.size() > this.MAX_ENTRIES;
    }
}

