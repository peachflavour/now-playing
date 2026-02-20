package com.nowplaying;

import lombok.Getter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NowPlayingTrackInfo {
    private static final Pattern COMPOSER_PATTERN = Pattern.compile("\\|composer[ \t]*=[ \t]*([a-zA-Z ,])\\|");
    private static final Pattern RELEASE_DATE_PATTERN = Pattern.compile("\\|release[ \t]*=[ \t]*\\[\\[([0-9]*)[ \t]*([a-zA-Z]*)]][ \t]*\\[\\[([0-9]*)]]");

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
        Matcher composerMatch = COMPOSER_PATTERN.matcher(responsePayload);
        composerMatch.
    }
}
