package com.nowplaying;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("example")
public interface NowPlayingConfig extends Config
{
	@ConfigItem(
			keyName = "fetch",
			name = "Fetch Wiki Contents",
			description = "Toggle whether Wiki information for a song should be retrieved"
	)
	default boolean fetch() { return false; };

	@ConfigItem(
			keyName = "overlayDuration",
			name = "Now playing overlay duration",
			description = "Sets the how long a new song is displayed as an overlay (in ms)"
	)
	default int overlayDuration() {return 3000;}

}
