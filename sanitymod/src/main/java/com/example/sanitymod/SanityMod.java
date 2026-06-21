package com.example.sanitymod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class SanityMod implements ModInitializer {

    public static final String MOD_ID = "sanitymod";

    @Override
    public void onInitialize() {
        PhobiaAssigner.register();
        SanityCommand.register();
        ServerTickEvents.END_SERVER_TICK.register(SanityTickHandler::onEndTick);
    }
}
