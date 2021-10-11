package com.oscarjco.fallblocks.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import org.kxml2.io.KXmlSerializer;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class RecordsList {
    private static FileHandle file;
    private Vector<Record> recordsList;

    public RecordsList(String gameMode) {
        File localFile = new File(Gdx.files.getLocalStoragePath() + gameMode + ".xml");
        if(!localFile.exists()) {
            FileOutputStream os = null;
            try {
                os = new FileOutputStream(localFile);
                String content = "<records-list></records-list>";
                os.write(content.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        file = Gdx.files.local(gameMode + ".xml");
        Gdx.app.log("localStorage", Gdx.files.getLocalStoragePath());
        recordsList = new Vector<Record>();
    }

    public void saveScore(String name, long score, Date date) {
        if (recordsList.isEmpty()) {
            try {
                readXML();
            } catch (Exception e) {
                Gdx.app.log("Error", "Failed to load records.");
                e.printStackTrace();
            }
        }

        if(recordsList.size() >= 10) {
            Collections.sort(recordsList, new Comparator() {
                @Override
                public int compare(Object o, Object t1) {
                    return (int) (((Record)t1).getScore() - ((Record)o).getScore());
                }
            });
            recordsList.remove(recordsList.lastElement());
        }

        recordsList.add(new Record(name, score, date));

        try {
            writeXML();
        } catch (Exception e) {
            Gdx.app.log("Error", "Failed to save records.");
            e.printStackTrace();
        }
    }

    public Vector<Record> getRecordsList() {
        if (recordsList.isEmpty()) {
            try {
                readXML();
            } catch (Exception e) {
                Gdx.app.log("error", "Failed to read records: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return recordsList;
    }

    public void writeXML() throws Exception {
        XmlSerializer serializer = new KXmlSerializer();

        serializer.setOutput(file.write(false), "UTF-8");
        serializer.startDocument("UTF-8", true);
        serializer.startTag("", "records-list");
        for (Record record : recordsList) {
            serializer.startTag("", "record");
            serializer.attribute("", "date",
                    String.valueOf(record.getDate().getTime()));
            serializer.startTag("", "name");
            serializer.text(record.getName());
            serializer.endTag("", "name");
            serializer.startTag("", "score");
            serializer.text(String.valueOf(record.getScore()));
            serializer.endTag("", "score");
            serializer.endTag("", "record");
        }
        serializer.endTag("", "records-list");
        serializer.endDocument();
    }

    public void readXML() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        XMLReader reader = (XMLReader) parser.getXMLReader();
        reader.setContentHandler(new XMLHandler());
        reader.parse(new InputSource(file.read()));
    }

    private class XMLHandler extends DefaultHandler {
        private StringBuilder builder;
        private Record record;

        @Override
        public void startDocument() {
            builder = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String name, String qualifiedName, Attributes attr) {
            builder.setLength(0);
            if (name.equals("record")) {
                record = new Record("", 0, new Date(Long.parseLong(attr.getValue("date"))));
            }
        }

        @Override
        public void characters(char[] chars, int start, int length) {
            builder.append(chars, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qualifiedName) {
            if (localName.equals("score")) {
                record.setScore(Integer.parseInt(builder.toString()));
            } else if (localName.equals("name")) {
                record.setName(builder.toString());
            } else if (localName.equals("record")) {
                recordsList.add(record);
            }
        }

        @Override
        public void endDocument() {}
    }
}