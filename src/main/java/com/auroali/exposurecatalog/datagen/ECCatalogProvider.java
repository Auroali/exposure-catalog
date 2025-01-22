package com.auroali.exposurecatalog.datagen;

import com.auroali.exposurecatalog.common.catalog.CatalogEntry;
import com.auroali.exposurecatalog.datagen.builders.CatalogEntryBuilder;
import com.auroali.exposurecatalog.datagen.providers.ExposureCatalogProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;

import java.util.function.Consumer;

public class ECCatalogProvider extends ExposureCatalogProvider {
    public ECCatalogProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    protected void generate(Consumer<CatalogEntry> consumer) {
        CatalogEntryBuilder.builder(EntityType.ALLAY)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.SLIME)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.VILLAGER)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.CREEPER)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.CAMEL)
          .description(this::getDescription)
          .scale(0.85d)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.HORSE)
          .description(this::getDescription)
          .scale(0.9d)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.SKELETON_HORSE)
          .description(this::getDescription)
          .scale(0.9d)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.DONKEY)
          .description(this::getDescription)
          .scale(0.9d)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.MULE)
          .description(this::getDescription)
          .scale(0.9d)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.SKELETON)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.ZOMBIE)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.ARMOR_STAND)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.AXOLOTL)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.TROPICAL_FISH)
          .description(this::getDescription)
          .tag(tag -> {
              // byte 1 - size - (0-1)
              // byte 2 - pattern - (0-5)
              // byte 3 - body color (0-15)
              // byte 4 - pattern color (0-15)
              tag.putInt("Variant", 0x01_01_00_00);
          })
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.PUFFERFISH)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.COD)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.SALMON)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.SQUID)
          .description(this::getDescription)
          .scale(0.65d)
          .offset(0, 7, 0)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.GLOW_SQUID)
          .description(this::getDescription)
          .scale(0.65d)
          .offset(0, 7, 0)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.WARDEN)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.DOLPHIN)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.BAT)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.BEE)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.POLAR_BEAR)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.BLAZE)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.DROWNED)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.WITHER_SKELETON)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.STRAY)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.HUSK)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.SHULKER)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.SILVERFISH)
          .scale(0.7d)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.PILLAGER)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.VINDICATOR)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.EVOKER)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.RAVAGER)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.HOGLIN)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.PIGLIN)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.PIGLIN_BRUTE)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.ZOMBIFIED_PIGLIN)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.ZOGLIN)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.FOX)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.CHICKEN)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.WOLF)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.IRON_GOLEM)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.PARROT)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.PHANTOM)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.PANDA)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.MOOSHROOM)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.SNIFFER)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.WANDERING_TRADER)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.TRADER_LLAMA)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.LLAMA)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.WITCH)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.TURTLE)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.SNOW_GOLEM)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.RABBIT)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.CAT)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.GHAST)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.VEX)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.ENDER_DRAGON)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.WITHER)
          .description(this::getDescription)
          .offerTo(consumer);
        CatalogEntryBuilder.builder(EntityType.CAVE_SPIDER)
          .description(this::getDescription)
          .offerTo(consumer);
    }

    public Component getDescription(EntityType<?> type) {
        return Component.translatable(this.getDescriptionKey(type));
    }

    public String getDescriptionKey(EntityType<?> type) {
        return Util.makeDescriptionId("catalog.entry", BuiltInRegistries.ENTITY_TYPE.getKey(type));
    }
}
