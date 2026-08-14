package com.ledgerlib.core;

import java.util.*;

public class CollectionsUtil {
    public static <T> void copy(MyArrayList<? super T> dest, MyArrayList<? extends T> src) {
        for (T item : src) {
            dest.add(item);
        }
    }

    public static void collectionLength(Collection<String> collection) {
        for (String s: collection) {
            System.out.println(s.length());
        }
        System.out.println("----------------");
    }

    public static void removeX(List<String> list) {
        var iter = list.iterator();
        while (iter.hasNext()) {
            String next = iter.next();
            if (next.startsWith("X")) iter.remove();
        }
    }

    public static void main(String[] args) {
//        var test_col_1 = new ArrayList<>(Arrays.asList("Caiditme", "Concac", "Bulol"));
//        var test_col_2 = new HashSet<>(Arrays.asList("Concac", "Bulol"));
//        var test_col_3 = new PriorityQueue<>(Arrays.asList("Bulol", "1", "ssssssss"));


//        var test = new ArrayList<>(Arrays.asList("Xmedia", "Media", "Alolo"));
//        System.out.println(test);
//
//        removeX(test);
//
//        System.out.println(test);


    }
}
