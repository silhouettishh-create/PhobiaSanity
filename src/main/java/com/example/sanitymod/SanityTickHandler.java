package com.example.sanitymod;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

public class SanityTickHandler {

    private static final int TEN_SEC = 200;
    private static final int FIVE_SEC = 100;

    private static int counter10 = 0;
    private static int counter5 = 0;

    public static void onEndTick(MinecraftServer server) {
        counter10++;
        counter5++;

        boolean run10 = counter10 >= TEN_SEC;
        boolean run5 = counter5 >= FIVE_SEC;
        if (run10) counter10 = 0;
        if (run5) counter5 = 0;
        if (!run10 && !run5) return;

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            PhobiaType phobia = SanityManager.getPhobia(player);

            if (run10 && phobia == PhobiaType.NYCTOPHOBIA) tickNyctophobia(player);
            if (run5 && phobia == PhobiaType.THALASSOPHOBIA) tickThalassophobia(player);
            if (run10 && phobia == PhobiaType.CHROMOPHOBIA) tickChromophobia(player);
        }
    }

    private static void tickNyctophobia(ServerPlayer player) {
        ServerLevel level = (ServerLevel) player.level();
        BlockPos pos = player.blockPosition();
        int light = level.getMaxLocalRawBrightness(pos);
        if (light <= 3) {
            SanityManager.addSanity(player, -3.0f);
        }
    }

    private static void tickThalassophobia(ServerPlayer player) {
        ServerLevel level = (ServerLevel) player.level();
        boolean inBoat = player.getVehicle() instanceof Boat;
        boolean swimming = player.isInWater();
        if (!inBoat && !swimming) return;

        var biomeHolder = level.getBiome(player.blockPosition());
        ResourceKey<Biome> key = biomeHolder.unwrapKey().orElse(null);

        float drop = isOceanBiome(key) ? 3.0f : isRiverBiome(key) ? 2.0f : 1.0f;
        SanityManager.addSanity(player, -drop);
    }

    private static boolean isOceanBiome(ResourceKey<Biome> key) {
        if (key == null) return false;
        return key == Biomes.OCEAN || key == Biomes.DEEP_OCEAN
                || key == Biomes.WARM_OCEAN || key == Biomes.LUKEWARM_OCEAN
                || key == Biomes.COLD_OCEAN || key == Biomes.FROZEN_OCEAN
                || key == Biomes.DEEP_LUKEWARM_OCEAN || key == Biomes.DEEP_COLD_OCEAN
                || key == Biomes.DEEP_FROZEN_OCEAN;
    }

    private static boolean isRiverBiome(ResourceKey<Biome> key) {
        if (key == null) return false;
        return key == Biomes.RIVER || key == Biomes.FROZEN_RIVER;
    }

    private static void tickChromophobia(ServerPlayer player) {
        SanityManager.addSanity(player, -0.25f);

        ServerLevel level = (ServerLevel) player.level();
        long t = level.getLevelData().getDayTime() % 24000;
        boolean isNight = t >= 13000 && t < 23000;

        if (isNight && !player.isSleeping()) {
            SanityManager.addSanity(player, 0.75f);
        }
    }
}
