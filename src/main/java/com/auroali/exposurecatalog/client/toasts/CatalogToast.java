package com.auroali.exposurecatalog.client.toasts;

import io.github.mortuusars.exposure.Exposure;
import io.github.mortuusars.exposure.item.CameraItem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class CatalogToast implements Toast {
    public static final Component TITLE = Component.translatable("exposurecatalog.toast.title");
    private static final long DISPLAY_TIME = 5000L;
    private static final ItemStack ICON = new ItemStack(Exposure.Items.CAMERA.get());
    private final int newEntries;

    public CatalogToast(int newEntries) {
        this.newEntries = newEntries;
    }

    @Override
    public Visibility render(GuiGraphics guiGraphics, ToastComponent toastComponent, long timeSinceLastVisible) {
        guiGraphics.blit(TEXTURE, 0, 0, 0, 0, this.width(), this.height());
        guiGraphics.drawString(toastComponent.getMinecraft().font, TITLE, 30, 7, 0xFFFFFF00, false);
        guiGraphics.drawString(toastComponent.getMinecraft().font, Component.translatable("exposurecatalog.toast.description", this.newEntries), 30, 18, 0xFFFFFFFF, false);

        guiGraphics.pose().pushPose();
        guiGraphics.renderFakeItem(ICON, 8, 8);
        guiGraphics.pose().popPose();

        return timeSinceLastVisible >= DISPLAY_TIME * toastComponent.getNotificationDisplayTimeMultiplier()
          ? Visibility.HIDE
          : Visibility.SHOW;
    }
}
