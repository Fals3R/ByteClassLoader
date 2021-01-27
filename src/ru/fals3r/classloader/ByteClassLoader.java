package ru.fals3r.classloader;

import com.sun.istack.internal.Nullable;
import ru.fals3r.utils.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ByteClassLoader extends URLClassLoader {

    private final HashMap<String, byte[]> jarEntries = new HashMap<>();

    public ByteClassLoader(URL[] urls, @Nullable ClassLoader parent, @Nullable byte[]... jarsBytes) throws IOException {
        super(urls, parent);

        if (jarsBytes != null) {
            for (byte[] byteArr : jarsBytes) {
                try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(byteArr))) {
                    ZipEntry zipEntry = zipInputStream.getNextEntry();
                    while (zipEntry != null) {
                        if(!zipEntry.isDirectory()) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            IOUtils.copy(zipInputStream, out);
                            jarEntries.put(zipEntry.getName(), out.toByteArray());
                        }
                        zipEntry = zipInputStream.getNextEntry();
                    }
                }
            }
        }
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        byte[] entryBytes = jarEntries.get(name.replace('.', '/') + ".class");
        if (entryBytes != null) {
            return this.defineClass(name, entryBytes, 0, entryBytes.length);
        }
        return super.loadClass(name);
    }

    @Override
    public URL findResource(String name) {
        if (jarEntries.containsKey(name)) {
            try {
                return new URL(null, "resource:/" + name, new ByteURLStreamHandler(jarEntries));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return super.findResource(name);
    }
}
