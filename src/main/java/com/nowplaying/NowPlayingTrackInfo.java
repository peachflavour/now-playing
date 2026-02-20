package com.nowplaying;

import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NowPlayingTrackInfo {
    private static final Pattern COMPOSER_PATTERN = Pattern.compile("\\Q|\\Ecomposer[ \\t]*=[ \\t]*(?<comp>[a-zA-Z ,]*).*\\Q|\\E");
    private static final Pattern RELEASE_DATE_PATTERN = Pattern.compile("\\Q|\\Erelease[ \\t]*=[ \\t]*\\[\\[(?<day>[0-9]*)[ \\t]*(?<mon>[a-zA-Z]*)]][ \\t]*\\[\\[(?<year>[0-9]*)]].*\\Q|\\E");

    @Getter
    private String releaseDate;

    @Getter
    private String locations;

    @Getter
    private String composer;

    // Parsing the trivia from the json payload is probably too much work?
//    @Getter
//    private ArrayList<String> trivia;

    public static NowPlayingTrackInfo fromWikiPayload(String responsePayload) {
        NowPlayingTrackInfo trackInfo = new NowPlayingTrackInfo();

        trackInfo.composer = COMPOSER_PATTERN.matcher(responsePayload).group("comp");
        Matcher releaseDateMatcher = RELEASE_DATE_PATTERN.matcher(responsePayload);
        trackInfo.releaseDate = releaseDateMatcher.group("mon") + ' ' + releaseDateMatcher.group("day") + ", " +releaseDateMatcher.group("year");

        return trackInfo;
    }
}
