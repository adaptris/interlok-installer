package com.adaptris.fxinstaller.utils;

import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;

public class XmlUtilsTest {

  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  @Test
  public void testGetDocument() throws Exception {
    Document document = XmlUtils.getDocument(getClass().getResource("/interlok-json.xml").toString());

    assertNotNull(document);
    assertNotNull("project", document.getNodeName());
  }

  @Test
  public void testGetDocumentInvalidXml() throws Exception {
    expectedEx.expect(SAXParseException.class);

    XmlUtils.getDocument(getClass().getResource("/invalid-xml.xml").toString());
  }

  @Test
  public void testGetDocumentInvalidLocation() throws Exception {
    expectedEx.expect(MalformedURLException.class);

    XmlUtils.getDocument("/invalid-location.xml");
  }

}
