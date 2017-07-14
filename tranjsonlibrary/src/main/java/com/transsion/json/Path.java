package com.transsion.json;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class Path {
    final LinkedList<String> path = new LinkedList<>();

    public Path() {
    }

    public Path(String... fields) {
        Collections.addAll(path, fields);
    }

    public Path enqueue(String field) {
        path.add(field);
        return this;
    }

    public String pop() {
        return path.removeLast();
    }

    public List<String> getPath() {
        return path;
    }

    public int length() {
        return path.size();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("[ ");
        boolean afterFirst = false;
        for (String current : path) {
            if (afterFirst) {
                builder.append(".");
            }
            builder.append(current);
            afterFirst = true;
        }
        builder.append(" ]");
        return builder.toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path1 = (Path) o;
        return path.equals(path1.path);
    }

    public int hashCode() {
        return path.hashCode();
    }

    public static Path parse(String path) {
        return path != null ? new Path( path.split("\\." ) ) : new Path();
    }
}
