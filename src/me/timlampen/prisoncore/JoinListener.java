package me.timlampen.prisoncore;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

/**
 * Created by Primary on 2/27/2016.
 */
public class JoinListener implements Listener{
    FakePlayers p;

    public JoinListener(FakePlayers p){
        this.p = p;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player origPlayer = event.getPlayer();
        for(String s : p.onlineFakePlayers) {
            GameProfile profile = new GameProfile(UUID.randomUUID(), s);
            MinecraftServer server = MinecraftServer.getServer();
            WorldServer world = server.getWorldServer(0);
            PlayerInteractManager manager = new PlayerInteractManager(world);
            EntityPlayer player = new EntityPlayer(server, world, profile, manager);
          //  Bukkit.getLogger().info("Created player variables");

            PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, player);
            //  Bukkit.getLogger().info("Created packet");

            ((CraftPlayer) origPlayer).getHandle().playerConnection.sendPacket(packet);
            // Bukkit.getLogger().info("Sent packet");
        }
    }
}
