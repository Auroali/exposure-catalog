package com.auroali.exposurecatalog.common.network;

import com.auroali.exposurecatalog.ExposureCatalog;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;

public record CatalogToastS2C(int numNewEntries) implements FabricPacket {
    public static final PacketType<CatalogToastS2C> ID = PacketType.create(ExposureCatalog.id("catalog_toast"), CatalogToastS2C::new);

    public CatalogToastS2C(FriendlyByteBuf buf) {
        this(buf.readVarInt());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(this.numNewEntries);
    }

    @Override
    public PacketType<?> getType() {
        return ID;
    }
}
