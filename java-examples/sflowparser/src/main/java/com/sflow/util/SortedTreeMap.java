package com.sflow.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;


public class SortedTreeMap<K, V> {

     SortedMap<K,V>           model;

     public SortedTreeMap() {

          Comparator<? super K> comparator
               = new Comparator<K>() {
               @Override
               public int compare(K o1, K o2) {    
                    if (o1 instanceof Integer) {
                         return (Integer)o1 - (Integer)o2;
                    }
                    
                    if (o1 instanceof Double) {
                         return (int) ((Double)o1 - (Double)o2);
                    }
                    
                    return o1.toString().compareTo(o2.toString());
               }
          };

          model = new TreeMap<K,V>(comparator);

     }
     
     public SortedTreeMap(Comparator<K> comparator) {
          model = new TreeMap<K,V>(comparator);
     }

     public int getSize() {
          return model.size();
     }

     public void put(K key, V element) {
          model.put(key, element);
     }

     public void clear() {
          model.clear();
     }

     public boolean containsKey(K key) {
          return model.containsKey(key);
     }

     public K firstKey() {
          return model.firstKey();
     }

     public Iterator<V> iterator() {
          return model.values().iterator();
     }

     public K lastKey() {
          return model.lastKey();
     }
     
     public V get(K key) {
          return model.get(key);
     }
     
     public boolean isEmpty() {
          return model.isEmpty();
     }
     
     public int size() {
          return model.size();
     }
     
     public V remove(K key) {
          return model.remove(key);
     }
     
}

