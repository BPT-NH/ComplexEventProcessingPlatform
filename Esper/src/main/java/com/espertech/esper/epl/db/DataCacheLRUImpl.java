/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.db;

import com.espertech.esper.collection.MultiKeyUntyped;
import com.espertech.esper.epl.join.table.EventTable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Query result data cache implementation that uses a least-recently-used algorithm
 * to store and evict query results.
 */
public class DataCacheLRUImpl implements DataCache
{
    private final int cacheSize;
    private static final float hashTableLoadFactor = 0.75f;
    private final LinkedHashMap<Object, EventTable> cache;

    /**
     * Ctor.
     * @param cacheSize is the maximum cache size
     */
    public DataCacheLRUImpl(int cacheSize)
    {
        this.cacheSize = cacheSize;
        int hashTableCapacity = (int)Math.ceil(cacheSize / hashTableLoadFactor) + 1;
        this.cache = new LinkedHashMap<Object,EventTable>(hashTableCapacity, hashTableLoadFactor, true)
        {
            private static final long serialVersionUID = 1;

            @Override protected boolean removeEldestEntry (Map.Entry<Object,EventTable> eldest)
            {
                return size() > DataCacheLRUImpl.this.cacheSize;
            }
        };
    }

    /**
    * Retrieves an entry from the cache.
    * The retrieved entry becomes the MRU (most recently used) entry.
    * @param lookupKeys the key whose associated value is to be returned.
    * @return the value associated to this key, or null if no value with this key exists in the cache.
    */
    public EventTable getCached(Object[] lookupKeys)
    {
        Object key = DataCacheUtil.getLookupKey(lookupKeys);
        return cache.get(key);
    }

    /**
    * Adds an entry to this cache.
    * If the cache is full, the LRU (least recently used) entry is dropped.
    * @param keys the keys with which the specified value is to be associated.
    * @param value a value to be associated with the specified key.
    */
    public synchronized void put(Object[] keys, EventTable value)
    {
        Object key = DataCacheUtil.getLookupKey(keys);
        cache.put(key, value);
    }

    /**
     * Returns the maximum cache size.
     * @return maximum cache size
     */
    public int getCacheSize()
    {
        return cacheSize;
    }

    public boolean isActive()
    {
        return true;
    }
}
