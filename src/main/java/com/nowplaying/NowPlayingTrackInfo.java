package com.nowplaying;

import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NowPlayingTrackInfo {
    private static final String INTERNAL_LINK = "\\[\\[(?<value>.*)]]";

    // |composer = Ashleigh Bridges\n|
    private static final Pattern COMPOSER_PATTERN = Pattern.compile("composer\\s*=\\s*(?<comp>[a-zA-Z \t']+)\\s*\\|");
    private static final Pattern RELEASE_DATE_PATTERN = Pattern.compile("release[ \t]*=[ \t]*\\[\\[(?<day>[0-9]*)[ \t]*(?<mon>[a-zA-Z]*)]][ \t]*\\[\\[(?<year>[0-9]*)]].*\\Q|\\E");
    private static final Pattern LOCATION_PATTERN = Pattern.compile("location\\s*=\\s*(?<loc>.*?|\\[\\[.*?]])(?=\\\\n\\s*\\||\\\\n\\s*})");

    @Getter
    private String releaseDate = "N/a";

    @Getter
    private String unlockLocation = "N/a";

    @Getter
    private String composer = "N/a";

    // Parsing the trivia from the json payload is probably too much work?
//    @Getter
//    private ArrayList<String> trivia;

    public static NowPlayingTrackInfo fromWikiPayload(String responsePayload) {
        NowPlayingTrackInfo trackInfo = new NowPlayingTrackInfo();

        Matcher composerMatcher = COMPOSER_PATTERN.matcher(responsePayload);
        if (composerMatcher.find()) {
            trackInfo.composer = trackInfo.cleanGroupNewlines(composerMatcher.group("comp"));
        }

        Matcher releaseDateMatcher = RELEASE_DATE_PATTERN.matcher(responsePayload);
        if (releaseDateMatcher.find()) {
            trackInfo.releaseDate = releaseDateMatcher.group("mon") + ' ' + releaseDateMatcher.group("day") + ", " +releaseDateMatcher.group("year");
        }

        Matcher locationMatcher = LOCATION_PATTERN.matcher(responsePayload);
        if (locationMatcher.find()) {
            trackInfo.unlockLocation = trackInfo.cleanGroupNewlines(locationMatcher.group("loc"));
        }

        return trackInfo;
    }

    private String cleanGroupNewlines(String value) {
        return value.replaceAll("\n", " ");
    }
}
