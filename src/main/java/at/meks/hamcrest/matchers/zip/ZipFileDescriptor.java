package at.meks.hamcrest.matchers.zip;

import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

class ZipFileDescriptor {

    final ZipFile zipFile;
    final Map<String, ZipEntry> entries = new HashMap<>();

    ZipFileDescriptor(ZipFile zipFile) {
        this.zipFile = zipFile;
        setEntries();
    }

    private void setEntries() {
        Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
        while (zipEntries.hasMoreElements()) {
            ZipEntry entry = zipEntries.nextElement();
            entries.put(Paths.get(entry.getName()).toString(), entry);
        }
    }

}
