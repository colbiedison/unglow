package us.dison.unglow.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import us.dison.unglow.config.UnglowConfig;

import java.io.IOException;

import static us.dison.unglow.Unglow.LOGGER;

@Environment(EnvType.CLIENT)
public class UnglowClient implements ClientModInitializer {

    private static final UnglowConfig CONFIG = new UnglowConfig(FabricLoader.getInstance().getConfigDir().resolve("unglow.properties"));

    private static KeyBinding toggleKeybinding = null;

    @Override
    public void onInitializeClient() {
        toggleKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.unglow.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "category.unglow.keybindings"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKeybinding.wasPressed()) {
                CONFIG.setEnabled(!CONFIG.isEnabled());
                if (client.player != null)
                    client.player.sendMessage( CONFIG.isEnabled() ?
                            Text.translatable("message.unglow.enabled") :
                            Text.translatable("message.unglow.disabled")
                    );
            }
        });

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            try {
                CONFIG.load();
            } catch (IOException e) {
                LOGGER.error("Failed to load config file!");
                CONFIG.setEnabled(true);
            }
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            try {
                CONFIG.save();
            } catch (IOException e) {
                LOGGER.error("Failed to save config file!");
                e.printStackTrace();
            }
        });
    }

    public static boolean isModEnabled() {
        return CONFIG.isEnabled();
    }
}
