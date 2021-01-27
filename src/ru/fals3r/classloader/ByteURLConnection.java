package ru.fals3r.classloader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class ByteURLConnection extends URLConnection {

    private final HashMap<String, byte[]> jarEntries;

    protected ByteURLConnection(URL url, HashMap<String, byte[]> jarEntries) {
        super(url);
        this.jarEntries = jarEntries;
    }

    @Override
    public void connect() {
    }

    @Override
    public InputStream getInputStream() {
        byte[] byteArr = jarEntries.get(this.getURL().getPath().substring(1));
        if (byteArr != null) {
            return new ByteArrayInputStream(byteArr);
        }
        return null;
    }
}
