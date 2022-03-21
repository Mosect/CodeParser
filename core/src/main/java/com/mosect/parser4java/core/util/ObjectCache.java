package com.mosect.parser4java.core.util;

import java.util.List;

/**
 * 对象缓存
 */
public class ObjectCache<T> {

    private final T[] objects;
    private int offset = 0;
    private int count = 0;
    private final ObjectCreator<T> creator;

    public ObjectCache(int maxCount, ObjectCreator<T> creator) {
        objects = (T[]) new Object[maxCount];
        this.creator = creator;
    }

    /**
     * 获取一个对象
     *
     * @return 对象
     */
    public T get() {
        if (count > 0) {
            T obj = objects[offset];
            objects[offset] = null;
            int next = offset + 1;
            if (next >= objects.length) {
                offset = next % objects.length;
            } else {
                offset = next;
            }
            return obj;
        }
        return creator.createObject();
    }

    /**
     * 返还一个对象
     *
     * @param obj 对象
     * @return true，返还成功；false，返回失败，容器已装满
     */
    public boolean put(T obj) {
        if (count < objects.length) {
            int index = moveIndex();
            objects[index] = obj;
            return true;
        }
        return false;
    }

    /**
     * 返回列表所有对象
     *
     * @param list 对象列表
     * @return 成功返还对象的数量
     */
    public int putAll(List<T> list) {
        int safeCount = Math.min(objects.length - count, list.size());
        if (safeCount > 0) {
            for (T obj : list) {
                int index = moveIndex();
                objects[index] = obj;
            }
            return safeCount;
        }
        return 0;
    }

    private int moveIndex() {
        int index = offset + count;
        if (index >= objects.length) {
            index %= objects.length;
        }
        ++count;
        return index;
    }

    public interface ObjectCreator<T> {
        T createObject();
    }
}
