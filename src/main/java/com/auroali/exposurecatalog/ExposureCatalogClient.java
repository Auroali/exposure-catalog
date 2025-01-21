package com.auroali.exposurecatalog;

import com.auroali.exposurecatalog.client.screens.CatalogScreen;
import com.auroali.exposurecatalog.client.toasts.CatalogToast;
import com.auroali.exposurecatalog.common.network.CatalogToastS2C;
import com.auroali.exposurecatalog.common.network.SyncCatalogEntriesS2C;
import com.auroali.exposurecatalog.common.registry.ECRegistries;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

public class ExposureCatalogClient implements ClientModInitializer {
    public static final KeyMapping OPEN_CATALOG = new KeyMapping(
      "key.exposurecatalog.open_catalog",
      InputConstants.Type.KEYSYM,
      InputConstants.KEY_O,
      "key.categories.gameplay"
    );

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(OPEN_CATALOG);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_CATALOG.consumeClick()) {
                client.setScreen(new CatalogScreen());
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(SyncCatalogEntriesS2C.ID, (packet, player, packetSender) ->
          ECRegistries.CATALOG.load(packet.entries())
        );

        ClientPlayNetworking.registerGlobalReceiver(CatalogToastS2C.ID, (packet, localPlayer, packetSender) -> {
            Minecraft.getInstance().getToasts().addToast(new CatalogToast(packet.numNewEntries()));
        });
    }
}
