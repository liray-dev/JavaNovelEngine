package jne.engine.utils;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class Discord {

    public static DiscordRPC instance = DiscordRPC.INSTANCE;
    public static Long startTimestamp = System.currentTimeMillis() / 1000;
    public static String state = "Scene Editor";
    public static String details = "Java Novel Engine";
    public static String largeImageKey = "logo";
    public static String largeImageText = "https://discord.gg/K5n6XeMnVU";
    public static String smallImageText = "https://discord.gg/K5n6XeMnVU";
    public static String partyId = "ae488379-351d-4a4f-ad32-2b9b01c91657";
    public static String joinSecret = "MTI4NzM0OjFpMmhuZToxMjMxMjM= ";

    public static void launch() {
        instance.Discord_Initialize("1060908435455099030", new DiscordEventHandlers(), true, "");
        update();
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                instance.Discord_RunCallbacks();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "JNE").start();
    }

    public static void update() {
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = startTimestamp;
        presence.state = state;
        presence.details = details;
        presence.largeImageKey = largeImageKey;
        presence.largeImageText = largeImageText;
        presence.smallImageText = smallImageText;
        presence.partyId = partyId;
        presence.joinSecret = joinSecret;
        instance.Discord_UpdatePresence(presence);
    }

}

