package com.sflow.util;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.Vector;

import org.apache.commons.collections4.trie.PatriciaTrie;


/* keeps a list of addresses in a patricia tree */
public class AddressList<T> {

     private PatriciaTrie<T> nodes;
     
     public final static AddressList<Address> ANY = new AddressList<Address>();

     public AddressList() {
          nodes = new PatriciaTrie<T>();
     }
     
     /**
      * Add a key-value pair, with the key-type being the ipv4 Address class
      * 
      * @param a
      * @param value
      */
     public void addNode(Address a, T value) {
          String key;

          if (nodes == null || this == ANY) {
               return;
          }

          try {
               key = new String(a.getBytes(), "ISO-8859-1");
               nodes.put(key, value);
          } catch (UtilityException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
          } catch (UnsupportedEncodingException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
          }
     }
     
     /**
      * Remove the key-value pair from the trie. key being the address in 'a'
      * 
      * @param a
      */
     public T removeNode(Address a) {
          String key;

          if (nodes == null || this == ANY) {
               return null;
          }

          try {
               key = new String(a.getBytes(), "ISO-8859-1");
               return nodes.remove(key);
          } catch (UtilityException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
          } catch (UnsupportedEncodingException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
          }
          return null;
     }

     /**
      * 
      * Add a key - value pair to the trie..
      * 
      * @param a
      * @param value
      * 
      */
     public void addNode(String a, T value) {
          String key;

          if (nodes == null || this == ANY) return;
          
          try {
               key = new String(a.getBytes(), "ISO-8859-1");
               nodes.put(key, value);
          } catch (UnsupportedEncodingException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
          }          
     }
     
     /**
      * Remove the key-value pair for the key in 'a'
      * 
      * @param a
      */
     public boolean removeNode(String a) {
          String key;

          if (nodes == null|| this == ANY) 
               return false;
          
          try {
               key = new String(a.getBytes(), "ISO-8859-1");
               if (nodes.remove(key) != null) return true;
          } catch (UnsupportedEncodingException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
          }     
          return false;
     }

     @SuppressWarnings("unchecked")
     public void addAddr(String ipAddr) {
          Address ip;
          
          if (nodes == null || this == ANY) return;
          
          try {
               ip = new Address(ipAddr);

               addNode(ip, (T) ip);
          } catch (UtilityException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
          }
     }
     
     public void addAddr(String ipAddr, T val) {
          Address ip;
          
          if (nodes == null || this == ANY) return;
          
          try {
               ip = new Address(ipAddr);
               addNode(ip, val);
          } catch (UtilityException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
          }
     }
     
     public boolean removeAddr(String ipAddr) {
          Address ip;
          
          if (nodes == null || this == ANY) return false;

          try {
               ip = new Address(ipAddr);
               if (removeNode(ip) != null)
                    return true;
          } catch (UtilityException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
          }
          
          return false;
     }

     public void addList(ArrayList<String> iplist, ArrayList<T> val) {          
          int i = 0;
          
          if (nodes == null || this == ANY) {
               return;
          }
          
          Iterator<String> iter = iplist.iterator();
          while (iter.hasNext()) {
               String ip = iter.next();
               try {
                    addNode(new Address(ip), val.get(i));
               } catch (UtilityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    break;
               }
               i++;
          }
     }
     
     @SuppressWarnings("unchecked")
     public void addList(ArrayList<Address> iplist) {
          
          if (nodes == null || this == ANY) {
               return;
          }
          
          Iterator<Address> iter = iplist.iterator();
          while (iter.hasNext()) {
               Address ip = iter.next();
               try {
                    addNode(ip, (T)ip);
               } catch (Exception e) {
                    e.printStackTrace();
               }
          }
     }
     
     public void addList(String[] keys, T[] values) {
          
          if (nodes == null || this == ANY) {
               return;
          }
          
          for (int i = 0; i < keys.length; i++) {
               String ip = keys[i];
               try {
                    addNode(new Address(ip), values[i]);
               } catch (UtilityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
               }
          }
     }

     public void addRange(Address from, Address to, T value) {
          Address next = from;
          
          addNode(from, value);
          while (true) {
               try {
                    next = Address.next(next);
                    if (next == null || next.equals(to)) {
                         break;
                    }
                    addNode(next, value);
               } catch (UtilityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    break;
               }
          }
          addNode(to, value);          
     }
     
     public boolean deleteRange(Address from, Address to) {
          Address next = from;
          boolean status = true;
          
          if (removeNode(from) == null) {
               status = false;
          }
          while (true) {
               try {
                    next = Address.next(next);
                    if (next == null || next.equals(to)) {
                         break;
                    }
                    if (removeNode(next) == null) {
                         status = false;
                    }
               } catch (UtilityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    break;
               }
          }
          if (removeNode(to) == null) {
               status = false;
          }     
          
          return status;
     }
     

     /**
      * returns if a particular ip address is present in the trie..
      * 
      * @param a
      * @return
      */
     public boolean containsKey(Address a) { 
          if (this == ANY) 
               return true;
          
          if (nodes == null ) {
               return false;
          }

          try {
               return nodes.containsKey(new String(a.getBytes(), "ISO-8859-1"));
          } catch (UtilityException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
          } catch (UnsupportedEncodingException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
          }
          return false;
     }
     
     public boolean containsAddr(String ipAddr) {
          Address ip;
          
          if (this == ANY) return true;
          if (nodes == null) return false;
          
          try {
               ip = new Address(ipAddr);
               return containsKey(ip);
          } catch (UtilityException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
          }
          return false;
     }
     

     public SortedMap<String, T> prefixMap(Address a) {

          if (nodes == null || this == ANY) {
               return null;
          }
          
          try {
               return nodes.prefixMap(new String(a.getBytes(), "ISO-8859-1"));
          } catch (UtilityException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
          } catch (UnsupportedEncodingException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
          }     
          return null;
     }
     
     public SortedMap<String, T> headMap(Address a) {

          if (nodes == null || this == ANY) {
               return null;
          }
          
          try {
               return nodes.headMap(new String(a.getBytes(), "ISO-8859-1"));
          } catch (UtilityException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
          } catch (UnsupportedEncodingException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
          }     
          return null;
     }

     public SortedMap<String, T> subMap(Address from, Address to) {

          if (nodes == null || this == ANY) return null;

          try {
               return nodes.subMap(
                         new String(from.getBytes(), "ISO-8859-1"),
                         new String(to.getBytes(), "ISO-8859-1"));
          } catch (UtilityException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
          } catch (UnsupportedEncodingException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
          }     
          return null;
     }
     
     public int numKeys() {
          if (nodes == null || this == ANY) 
               return 0;
          return nodes.keySet().size();
     }
     
     public int size() {
          return numKeys();
     }
     
     public Set<String> keySet(int len) {
          int i = 0;
          int keySize = numKeys();
          
          if (keySize < len || this == ANY) {
               return null;
          }
          
          HashSet<String> hs = new HashSet<String>();
          String[] retSet = new String[len];
          while (i < len) {
               if (i == 0) {
                    retSet[i] = nodes.firstKey();
               } else {
                    retSet[i] = nodes.nextKey(retSet[i-1]);
               }
               hs.add(retSet[i]);
               i++;
          }
          
          return hs;
     }

     public Set<String> keySet() {
          if (nodes == null || this == ANY) return null;
          return nodes.keySet();
     }
          

     public void printValues() {
          
          if (nodes == null || this == ANY) {
               return;
          }
          
          Collection<T> values = (Collection<T>) nodes.values();
          Iterator<T> iter = values.iterator();
          while (iter.hasNext()) {
               System.out.println(iter.next());
          }
     }
     
     public void toHex(byte[] buf) {
          System.out.print("----keyStr " + buf.length + " ");

          for (int j = 0; j < buf.length; j++) {
               System.out.printf("%x-", buf[j]);
          }
          System.out.println();
     }
     
     public void printKeys() {
          if (nodes == null || this == ANY) {
               return;
          }
          
             Set<String> keyset = nodes.keySet();                           
         if (keyset.size() != 0) {          
              System.out.println("keyset.size " +  keyset.size());
              for (int i = 0; i < keyset.size(); i++) {
                   String keystr = (String)keyset.toArray()[i];  
                   toHex(keystr.getBytes());
                
                   byte[] buf = null;
                    try {
                         buf = keystr.getBytes("ISO-8859-1");
                         toHex(buf);
                    } catch (UnsupportedEncodingException e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                    }
              }
         }
     }
     
     public void printKeysAsAddress() {
          if (nodes == null || this == ANY) {
               return;
          }
          
          Set<String> keyset = nodes.keySet();                           
          if (keyset.size() != 0) {          
              System.out.println("keyset.size " +  keyset.size());
              Object[] keysetArry = keyset.toArray(); 
              for (int i = 0; i < keyset.size(); i++) {
                   String keystr = (String)keysetArry[i];  
                
                   byte[] buf = null;
                    try {
                         buf = keystr.getBytes("ISO-8859-1");
                         System.out.println(new Address(buf));
                    } catch (UnsupportedEncodingException e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                    } 
                    catch (UtilityException e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                    } 
              }
         }
     }
     
     /**
      * returns all the addresses in this list.
      * 
      * @return
      */
     public Vector<String> getAddrList() {
          if (nodes == null || this == ANY) 
               return null;
          
             Set<String> keyset = nodes.keySet();                           
             Address a = new Address();
          if (keyset.size() != 0) {          
              System.out.println("keyset.size " +  keyset.size());
               Vector<String> resultSet = new Vector<String>(keyset.size());
              Object[] keysetArry = keyset.toArray(); 
              for (int i = 0; i < keyset.size(); i++) {
                   String keystr = (String)keysetArry[i];       
                   byte[] buf = null;
                    try {
                         buf = keystr.getBytes("ISO-8859-1");
                         a.init(buf);
                         resultSet.add(a.toString());
                    } catch (UnsupportedEncodingException e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                    } 
                    catch (UtilityException e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                    } 
              }
              return resultSet;
         }
          
          return null;
     }
     
     /**
      * returns all the addresses filtered by the addr param.
      * addr param can be enterred as 10.1.1.1 or 10.1.*.*
      * 
      * @param addr
      * @return
      * @throws UtilityException
      */
     public Vector<String> getAddrList(String addr) throws UtilityException  {
          if (nodes == null || this == ANY) 
               return null;
          
          SortedMap<String, T> map = prefixMap(new Address(addr));                          
             Address a = new Address();
          if (map.size() != 0) {          
               Vector<String> resultSet = new Vector<String>(map.size());
               Iterator<String> mapIterator = map.keySet().iterator();
               while (mapIterator.hasNext()) {
                    byte[] buf = null;
                    try {
                         buf = mapIterator.next().getBytes("ISO-8859-1");
                         a.init(buf);
                         resultSet.add(a.toString());
                    } catch (UnsupportedEncodingException e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                    }
               }
              return resultSet;
          }
          
          return null;
     }
          
     
     public static void main(String[] args) {
          AddressList<String> l = new AddressList<String>();
          
          Address from;
          Address to;
          try {
               from = new Address("10.1.*.*");
               to = new Address("10.2.255.255");     
                         
               l.addRange(from, to, "20.*.*.*-20.255.255.255");
               
//               System.out.println("contains 10.0.0.50 ? " + l.containsAddr("10.0.0.50"));
          
               Vector<String> vec = l.getAddrList("10.1.1.*");
               for (int i = 0; i < vec.size(); i++) {
                    System.out.println(vec.get(i));
               }
               
          } catch (UtilityException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
          }     
     }
}
