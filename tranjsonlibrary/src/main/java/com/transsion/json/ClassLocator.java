package com.transsion.json;

public interface ClassLocator {
    Class locate(ObjectBinder context, Path currentPath) throws ClassNotFoundException;
}
