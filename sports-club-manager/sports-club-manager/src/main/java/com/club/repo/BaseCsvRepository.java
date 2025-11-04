
package com.club.repo;

import com.club.interfaces.Persistable;
import com.club.util.CsvUtil;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public abstract class BaseCsvRepository<T> implements Persistable {
    protected String path;
    public BaseCsvRepository(String path){ this.path = path; }

    public abstract List<String[]> serializeAll();
    public abstract void deserializeAll(List<String[]> rows);

    public void save(){
        try {
            CsvUtil.writeAll(path, serializeAll());
        } catch (IOException e){
            throw new RuntimeException("Cannot write " + path, e);
        }
    }

    public void load(){
        try {
            List<String[]> rows = CsvUtil.readAll(path);
            deserializeAll(rows);
        } catch (IOException e){
            // file missing ok
            deserializeAll(new ArrayList<>());
        }
    }
}
