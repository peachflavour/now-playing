package com.nowplaying;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import okhttp3.*;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

@Slf4j
@PluginDescriptor(
		name = "Now Playing!"
)
public class NowPlayingPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private NowPlayingConfig config;

	@Inject
	private NowPlayingOverlay overlay;

	@Inject
	private OverlayManager overlayManager;

	@Getter
    private String lastSongName;

	@Getter
	private NowPlayingTrackInfo lastSongInfo;

	@Inject
	private OkHttpClient okHttpClient;

	private final HashMap<String, NowPlayingTrackInfo> songInfoCache = new HashMap<>();


    @Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
		this.lastSongName = "";
		this.lastSongInfo = null;
		log.debug("Example started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
		this.lastSongName = "";
		this.songInfoCache.clear();
		this.lastSongInfo = null;
		log.debug("Example stopped!");
	}

	@Subscribe
	public void onGameTick(GameTick tick) {
		Widget musicWidgetText = client.getWidget(InterfaceID.Music.NOW_PLAYING_TEXT);

		if (musicWidgetText == null) {
			log.debug("Music widget text not loaded or otherwise null");
			return;
		}

		String text = musicWidgetText.getText();

		if (!text.equals(this.lastSongName)) {
			this.lastSongName = text;
		} else {
			return;
		}

		if (config.fetch()) {
			if (songInfoCache.containsKey(text)) {
				this.lastSongInfo = songInfoCache.get(text);
			} else {
				String cleanSongName = text.replaceAll(" ", "_");
				fetchWikiData(cleanSongName);
			}
		}
		log.debug(text);
	}

	private void fetchWikiData(String songName) {
		HttpUrl url = new HttpUrl.Builder()
				.scheme("https")
				.host("oldschool.runescape.wiki")
				.addPathSegment("api.php")
				.addQueryParameter("action", "parse")
				.addQueryParameter("format", "json")
				.addQueryParameter("prop", "wikitext")
				.addQueryParameter("page", songName)
				.build();

		Request request = new Request.Builder().header("User-Agent", "Runelite / Now Playing! / @peachflavour").url(url).build();

		okHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				log.warn("Failed to retrieve song data from the wiki {}", songName);
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
                try (response) {
					if (!(response.body() == null)) {
						String body = response.body().string();
						NowPlayingTrackInfo songInfo = NowPlayingTrackInfo.fromWikiPayload(body);

						songInfoCache.put(songName, songInfo);
						lastSongInfo = songInfo;
					}
                } catch (NullPointerException e) {
                    log.warn(e.toString());
                }
			}
		});
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN) {
			// something here
		}
	}

	@Provides
	NowPlayingConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(NowPlayingConfig.class);
	}
}
