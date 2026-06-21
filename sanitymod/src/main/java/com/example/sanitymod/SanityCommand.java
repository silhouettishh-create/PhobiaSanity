package com.example.sanitymod;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SanityCommand {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("sanity")
                    .executes(context -> {
                        ServerPlayer player = context.getSource().getPlayerOrException();
                        float sanity = SanityManager.getSanity(player);
                        PhobiaType phobia = SanityManager.getPhobia(player);

                        player.sendSystemMessage(Component.literal(
                                "Sanity: " + Math.round(sanity) + "/100  |  Phobia: " + describe(phobia)));
                        return 1;
                    }));
        });
    }

    private static String describe(PhobiaType type) {
        return switch (type) {
            case NYCTOPHOBIA -> "Nyctophobia (fear of darkness)";
            case THALASSOPHOBIA -> "Thalassophobia (fear of deep water)";
            case CHROMOPHOBIA -> "Chromophobia (fear of time)";
            default -> "none";
        };
    }
}
