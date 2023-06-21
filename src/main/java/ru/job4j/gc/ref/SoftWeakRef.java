package ru.job4j.gc.ref;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class SoftWeakRef {
    public static void main(String[] args) {
        String first = "Hello ";
        String second = "World!";
        SoftReference<String> softReference = new SoftReference<>(first);
        WeakReference<String> weakReference = new WeakReference<>(second);
        String res1 = softReference.get();
        String res2 = weakReference.get();
        if (res1 != null && res2 != null) {
            System.out.println(res1 + res2);
        }
    }
}
