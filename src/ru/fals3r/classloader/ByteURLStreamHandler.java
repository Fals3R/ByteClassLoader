package ru.fals3r.classloader;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.HashMap;

public class ByteURLStreamHandler extends URLStreamHandler {

    private final HashMap<String, byte[]> jarEntries;

    public ByteURLStreamHandler(HashMap<String, byte[]> jarEntries) {
        this.jarEntries = jarEntries;
    }

    @Override
    protected URLConnection openConnection(URL u) {
        return new ByteURLConnection(u, jarEntries);
    }
}
