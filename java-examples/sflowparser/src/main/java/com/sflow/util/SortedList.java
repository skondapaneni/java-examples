package com.sflow.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class SortedList<E> {

     SortedSet<E>           model;

     public SortedList() {

          Comparator<? super E> comparator
               = new Comparator<E>() {
               @Override
               public int compare(E o1, E o2) {   
                    
                    if (o1 instanceof Integer) {
                         return (Integer)o1 - (Integer)o2;
                    }
                    
                    if (o1 instanceof Double) {
                         return (int) ((Double)o1 - (Double)o2);
                    }
                    
                    return o1.toString().compareTo(o2.toString());
               }
          };

          model = new TreeSet<E>(comparator);

     }
     
     public SortedList(Comparator<E> comparator) {
          model = new TreeSet<E>(comparator);
     }

     public int getSize() {
          return model.size();
     }

     @SuppressWarnings("unchecked")
     public E getElementAt(int index) {
          if (model.size() > index)
               return (E) model.toArray()[index];
          return null;
     }
     
     @SuppressWarnings("unchecked")
     public E get(int index) {
          if (model.size() > index)
               return (E) model.toArray()[index];
          return null;     
     }

     public void add(E element) {
          model.add(element);
     }

     public void addAll(E elements[]) {
          List<E> c = Arrays.asList(elements);
          model.addAll(c);
     }

     public void clear() {
          model.clear();
     }

     public boolean contains(Object element) {
          return model.contains(element);
     }

     public E firstElement() {
          if (model.size() != 0)
               return model.first();
          return null;
     }

     public Iterator<E> iterator() {
          return model.iterator();
     }

     public E lastElement() {
          if (model.size() != 0)
               return model.last();
          return null;
     }

     public boolean removeElement(E element) {
          return model.remove(element);
     }

     public int size() {
          // TODO Auto-generated method stub
          return model.size();
     }
}

