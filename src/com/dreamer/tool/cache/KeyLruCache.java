/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dreamer.tool.cache;

import android.util.LruCache;

import java.util.HashMap;

/**
 * An LRU cache that internally support querying the keys as well as values.  We use this to keep
 * track of the task metadata to determine when to invalidate the cache when tasks have been
 * updated. Generally, this cache will return the last known cache value for the requested task
 * key.
 */
public class KeyLruCache<V> {
    // We keep a set of keys that are associated with the LRU cache, so that we can find out
    // information about the Task that was previously in the cache.
    HashMap<Integer, CacheObject> mKeys = new HashMap<Integer, CacheObject>();
    // The cache implementation, mapping task id -> value
    LruCache<Integer, V> mCache;

    public KeyLruCache(int cacheSize) {
        mCache = new LruCache<Integer, V>(cacheSize) {

            @Override
            protected void entryRemoved(boolean evicted, Integer taskId, V oldV, V newV) {
            	mKeys.remove(taskId);
            }
        };
    }

    /** Gets a specific entry in the cache. */
    public final V get(CacheObject key) {
        return mCache.get(key.getId());
    }

    /**
     * Returns the value only if the Task has not updated since the last time it was in the cache.
     */
    public final V getAndInvalidateIfModified(CacheObject key) {
    	CacheObject lastKey = mKeys.get(key.id);
        if (lastKey != null && (lastKey.lastActiveTime < key.lastActiveTime)) {
            // The task has updated (been made active since the last time it was put into the
            // LRU cache) so invalidate that item in the cache
            remove(key);
            return null;
        }
        // Either the task does not exist in the cache, or the last active time is the same as
        // the key specified, so return what is in the cache
        return mCache.get(key.getId());
    }

    /** Puts an entry in the cache for a specific key. */
    public final void put(CacheObject key, V value) {
        mCache.put(key.getId(), value);
        mKeys.put(key.getId(), key);
    }

    /** Removes a cache entry for a specific key. */
    public final void remove(CacheObject key) {
        mCache.remove(key.getId());
        mKeys.remove(key.getId());
    }

    /** Removes all the entries in the cache. */
    public final void evictAll() {
        mCache.evictAll();
        mKeys.clear();
    }

    /** Returns the size of the cache. */
    public final int size() {
        return mCache.size();
    }

    /** Trims the cache to a specific size */
    public final void trimToSize(int cacheSize) {
        mCache.trimToSize(cacheSize);
    }
}
