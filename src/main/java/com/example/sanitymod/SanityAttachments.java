package com.example.sanitymod;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.ResourceLocation;

public class SanityAttachments {

    public static final AttachmentType<Float> SANITY = AttachmentRegistry.create(
            ResourceLocation.fromNamespaceAndPath(SanityMod.MOD_ID, "sanity"),
            builder -> builder.initializer(() -> 100.0f).persistent(Codec.FLOAT)
    );

    public static final AttachmentType<PhobiaType> PHOBIA = AttachmentRegistry.create(
            ResourceLocation.fromNamespaceAndPath(SanityMod.MOD_ID, "phobia"),
            builder -> builder.initializer(() -> PhobiaType.NONE)
                    .persistent(Codec.STRING.xmap(PhobiaType::valueOf, PhobiaType::name))
    );
}
