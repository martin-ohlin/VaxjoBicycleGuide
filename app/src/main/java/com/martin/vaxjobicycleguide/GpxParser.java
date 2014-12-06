/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.martin.vaxjobicycleguide;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class parses XML feeds from stackoverflow.com.
 * Given an InputStream representation of a feed, it returns a List of entries,
 * where each list element represents a single entry (post) in the XML feed.
 */
public class GpxParser {
    private static final String ns = null;

    private static final String TAG_GPX = "gpx";
    private static final String TAG_TRK = "trk";
    private static final String TAG_TRKSEG = "trkseg";
    private static final String TAG_TRKPT = "trkpt";

    // We don't use namespaces

    public List<Entry> parse(final InputStream in) throws XmlPullParserException, IOException {
        try {
            final XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readGpx(parser);
        } finally {
            in.close();
        }
    }

    private static List<Entry> readGpx(final XmlPullParser parser) throws XmlPullParserException, IOException {
        final List<Entry> entries = new ArrayList<Entry>(2048);

        parser.require(XmlPullParser.START_TAG, ns, TAG_GPX);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals(TAG_TRK)) {
                readTrack(parser, entries);
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private static void readTrack(final XmlPullParser parser, final List<Entry> entries) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_TRK);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals(TAG_TRKSEG)) {
                readTrackSegment(parser, entries);
            } else {
                skip(parser);
            }
        }
    }

    private static void readTrackSegment(final XmlPullParser parser, final List<Entry> entries) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_TRKSEG);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals(TAG_TRKPT)) {
                entries.add(readTrackPoint(parser));
            } else {
                skip(parser);
            }
        }
    }

    private static Entry readTrackPoint(final XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_TRKPT);
        final Entry entry = new Entry(Double.parseDouble(parser.getAttributeValue(ns, "lat")),
                Double.parseDouble(parser.getAttributeValue(ns, "lon")));

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            skip(parser);
        }

        return entry;
    }

    // This class represents a single entry (post) in the XML feed.
    // It includes the data members "title," "link," and "summary."
    public static class Entry {
        public final double latitude;
        public final double longitude;

        private Entry(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
    private static void skip(final XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
