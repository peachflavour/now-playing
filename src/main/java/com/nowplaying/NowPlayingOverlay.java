package com.nowplaying;

import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TextComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import net.runelite.client.ui.overlay.tooltip.Tooltip;

import javax.inject.Inject;

import java.awt.*;
import java.util.Objects;

public class NowPlayingOverlay extends OverlayPanel {

    private static final int TOOLTIP_RECT_SIZE_X = 200;

    private final Client client;
    private final NowPlayingPlugin plugin;
    private final NowPlayingConfig config;
    private final TooltipManager toolTipManager;
    private final Tooltip songToolTip = new Tooltip(new PanelComponent());

    @Inject
    private NowPlayingOverlay(Client client, NowPlayingPlugin plugin, NowPlayingConfig config, TooltipManager toolTipManager) {
        super(plugin);
        setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.toolTipManager = toolTipManager;
        this.songToolTip.getComponent().setPreferredSize(new Dimension(TOOLTIP_RECT_SIZE_X, 0));
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        String songName = plugin.getLastSongName();

        if (!Objects.equals(songName, "")) {
            songName = "Now playing: " + songName;
        }

        panelComponent.getChildren().add(TitleComponent.builder().text(songName).color(Color.YELLOW).build());
        panelComponent.setPreferredSize(new Dimension(graphics.getFontMetrics().stringWidth(songName) + 10, 0));

        if (config.fetch()) {
            NowPlayingTrackInfo info = plugin.getLastSongInfo();

            if (!(info == null)) {

                Point mousePosition = client.getMouseCanvasPosition();
                if (this.getBounds().contains(mousePosition.getX(), mousePosition.getY())) {
                    final PanelComponent songToolTip = (PanelComponent) this.songToolTip.getComponent();
                    songToolTip.getChildren().clear();
                    songToolTip.getChildren().add(LineComponent.builder()
                            .left("Composer:").right("Ian Taylor")
                            .left("Released:").right(info.)
                            .build());

                    toolTipManager.add(this.songToolTip);
                }
            }
        }

        return super.render(graphics);
    }
}
