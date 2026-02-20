package com.nowplaying;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class NowPlayingPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(NowPlayingPlugin.class);
		RuneLite.main(args);
	}
}