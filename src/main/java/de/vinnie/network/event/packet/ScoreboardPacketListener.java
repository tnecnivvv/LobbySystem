package de.vinnie.network.event.packet;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.score.ScoreFormat;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerScoreboardObjective;
import de.vinnie.network.ConfigManager;
import de.vinnie.network.config.ConfigTypes;
import de.vinnie.network.config.ConfigValues;

import java.util.Objects;

public class ScoreboardPacketListener implements PacketListener {

    @Override
    public void onPacketSend(PacketSendEvent event) {
        ConfigValues.ServerConfig serverConfig = Objects.requireNonNull(ConfigManager.getConfig(ConfigTypes.SETTINGS)).server;
        if(event.getPacketType() != PacketType.Play.Server.SCOREBOARD_OBJECTIVE) return;
        if(serverConfig.hideScoreboardNumbers) {
            WrapperPlayServerScoreboardObjective objective = new WrapperPlayServerScoreboardObjective(event);
            objective.setScoreFormat(ScoreFormat.blankScore());
            event.markForReEncode(true);
        }
    }
}