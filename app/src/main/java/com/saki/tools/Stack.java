package com.saki.tools;

import java.lang.reflect.Array;

public class Stack<T> {
    T[] stack;
    int index;

    public Stack(Class<T> type, int deep) {
        stack = (T[]) Array.newInstance(type, deep);
    }

    public void push(T t) {
        stack[index++] = t;
        if (index == stack.length)
            index = 0;
    }

    public boolean pushRemove(T t) {
        for (int i = 0; i < stack.length; i++) {
            if (stack[i] != null && stack[i].equals(t)) {
                stack[i] = null;
                return false;
            }
        }
        push(t);
        return true;
    }
}
