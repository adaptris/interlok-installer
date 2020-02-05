package com.adaptris.fxinstaller.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class ZipUtilsTest {

  private final URL resource;

  public ZipUtilsTest() {
    resource = getClass().getClassLoader().getResource("zip-test");
  }

  @After
  public void afterTest() throws Exception {
    File testFileZip = Paths.get(resource.toURI()).resolve("zip-test-file.txt" + ZipUtils.ZIP).toFile();
    Files.deleteIfExists(testFileZip.toPath());
  }

  @Test
  public void testZipFileList() throws Exception {
    File testFile = Paths.get(resource.toURI()).resolve("zip-test-file.txt").toFile();
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      ZipUtils.zipFileList(Collections.<File>singletonList(testFile), baos);
      // TODO better assert for baos
      Assert.assertNotNull(baos);
      Assert.assertEquals(161, baos.size());
      String baosString = new String(baos.toByteArray());
      Assert.assertTrue(baosString.contains("zip-test-file.txt"));
    }
  }

  @Test
  public void testZipFileListNullFile() throws Exception {
    assertThrows(NullPointerException.class, () -> {
      try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
        ZipUtils.zipFileList(Collections.<File> singletonList(null), baos);
      }
    });
  }

  @Test
  public void testZipFileListDuplicatedFile() throws Exception {
    ZipException exception = assertThrows(ZipException.class, () -> {
      File testFile = Paths.get(resource.toURI()).resolve("zip-test-file.txt").toFile();
      List<File> files = new ArrayList<>();
      files.add(testFile);
      files.add(testFile);
      try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
        ZipUtils.zipFileList(files, baos);
      }
    });
    assertEquals("duplicate entry: zip-test-file.txt", exception.getMessage());
  }

}
