package com.example.sanitymod;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SanityManager {

    private static final Map<UUID, ServerBossEvent> BARS = new HashMap<>();

    public static float getSanity(ServerPlayer player) {
        return player.getAttachedOrCreate(SanityAttachments.SANITY);
    }

    public static void setSanity(ServerPlayer player, float value) {
        float clamped = Math.max(0.0f, Math.min(100.0f, value));
        player.setAttached(SanityAttachments.SANITY, clamped);
        updateBar(player, clamped);
    }

    public static void addSanity(ServerPlayer player, float delta) {
        setSanity(player, getSanity(player) + delta);
    }

    public static PhobiaType getPhobia(ServerPlayer player) {
        return player.getAttachedOrCreate(SanityAttachments.PHOBIA);
    }

    public static void setPhobia(ServerPlayer player, PhobiaType phobia) {
        player.setAttached(SanityAttachments.PHOBIA, phobia);
    }

    private static ServerBossEvent getOrCreateBar(ServerPlayer player) {
        return BARS.computeIfAbsent(player.getUUID(), id -> {
            ServerBossEvent bar = new ServerBossEvent(
                    Component.literal("Sanity"),
                    BossEvent.BossBarColor.PURPLE,
                    BossEvent.BossBarOverlay.NOTCHED_10
            );
            bar.addPlayer(player);
            return bar;
        });
    }

    public static void updateBar(ServerPlayer player, float sanity) {
        ServerBossEvent bar = getOrCreateBar(player);
        bar.setProgress(sanity / 100.0f);
        bar.setColor(sanity > 60 ? BossEvent.BossBarColor.PURPLE
                : sanity > 30 ? BossEvent.BossBarColor.YELLOW
                : BossEvent.BossBarColor.RED);
        bar.setName(Component.literal("Sanity: " + Math.round(sanity) + "/100"));
    }

    public static void removePlayer(ServerPlayer player) {
        ServerBossEvent bar = BARS.remove(player.getUUID());
        if (bar != null) bar.removeAllPlayers();
    }
}
