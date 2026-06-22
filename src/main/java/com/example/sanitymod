package com.example.sanitymod;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Random;

public class PhobiaAssigner {

    private static final PhobiaType[] POOL = {
            PhobiaType.NYCTOPHOBIA, PhobiaType.THALASSOPHOBIA, PhobiaType.CHROMOPHOBIA
    };
    private static final Random RANDOM = new Random();

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.getPlayer();
            if (SanityManager.getPhobia(player) == PhobiaType.NONE) {
                PhobiaType chosen = POOL[RANDOM.nextInt(POOL.length)];
                SanityManager.setPhobia(player, chosen);
                player.sendSystemMessage(Component.literal("A creeping dread settles in... you have developed " + format(chosen) + "."));
            }
            SanityManager.updateBar(player, SanityManager.getSanity(player));
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
                SanityManager.removePlayer(handler.getPlayer()));
    }

    private static String format(PhobiaType type) {
        return switch (type) {
            case NYCTOPHOBIA -> "Nyctophobia (fear of darkness)";
            case THALASSOPHOBIA -> "Thalassophobia (fear of deep water)";
            case CHROMOPHOBIA -> "Chromophobia (fear of time)";
            default -> "no phobia";
        };
    }
}
