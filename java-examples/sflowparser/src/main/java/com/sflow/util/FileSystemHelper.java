package com.sflow.util;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystems;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;


@Service
public class FileSystemHelper {
     
     private HashMap<URI, FileSystem> hmap = new HashMap<URI, FileSystem>();
     
     @PostConstruct
     public void init() {
     }

     public FileSystem getFileSystem(URI uri) {
          FileSystem fileSystem = null;
          
          try {
               fileSystem = FileSystems.newFileSystem
                         (uri, Collections.<String, Object>emptyMap());
          } catch (FileSystemAlreadyExistsException fse) {
               fileSystem  = FileSystems.getFileSystem(uri);
          } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
          }
          
          hmap.put(uri,  fileSystem);
          return fileSystem;     
     }
     
     public void close() {
          Iterator<FileSystem> iter = hmap.values().iterator();
          while (iter.hasNext()) {
               try {
                    iter.next().close();
               } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
               }
          }
     }

}
