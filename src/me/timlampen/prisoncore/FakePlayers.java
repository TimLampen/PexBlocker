package me.timlampen.prisoncore;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedServerPing;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class FakePlayers extends JavaPlugin{
    public static Plugin p;
    private API api;
    private Logger logger;
    int maxFakeOnline = 3;
    String[] fakeNames = new String[]{"charlie", "xXxScoperxXx", "UrMom", "Hola", "Cookie", "Shirt"};
    ArrayList<String> onlineFakePlayers = new ArrayList<String>();
    public void onEnable(){
        p = this;
        logger = getLogger();
        Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);
        getCommand("fakeplayers").setExecutor(new FakePlayerCommand(this));
        getCommand("fp").setExecutor(new FakePlayerCommand(this));
        if(getServer().getPluginManager().getPlugin("ProtocolLib") == null){
            log("ProtocolLib was not found");
            log("Please install ProtocolLib or this plugin will not work.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        getDataFolder().mkdirs();

        api = new API(this);
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(PacketAdapter.params(this, new PacketType[]{PacketType.Status.Server.OUT_SERVER_INFO}).optionAsync()){
            public void onPacketSending(PacketEvent event){
                WrappedServerPing ping = (WrappedServerPing) event.getPacket().getServerPings().read(0);
                ping.setPlayersOnline(api.getRealOnline() + api.getFakeOnline());
                ping.setPlayersMaximum(api.getMaxPlayers());
                ping.setPlayersVisible(true);
            }
        });


        new BukkitRunnable(){
            @Override
            public void run(){
                String fakeName = fakeNames[new Random().nextInt(fakeNames.length)];
                if(onlineFakePlayers.size()==fakeNames.length){
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "All fake players are online!");

                }
                else if(onlineFakePlayers.size()==maxFakeOnline){
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Max fake players");
                }
                else{
                    while(onlineFakePlayers.contains(fakeName)){
                        fakeName = fakeNames[new Random().nextInt(fakeNames.length)];
                    }
                    Bukkit.broadcastMessage(ChatColor.YELLOW + fakeName + " has joined the server.");
                    createPlayer(fakeName);
                    onlineFakePlayers.add(fakeName);
                }

            }
        }.runTaskTimer(this, 0, 20*10);

        new BukkitRunnable(){
            @Override
            public void run(){

            }
        }.runTaskTimer(this, 0, 20*15);
    }

    protected void help(CommandSender sender){
        sender.sendMessage(ChatColor.GOLD + "/fp setonline [integer]");
        sender.sendMessage(ChatColor.AQUA + "Sets the fake online player");
        sender.sendMessage(ChatColor.GOLD + "/fp setmax [integer]");
        sender.sendMessage(ChatColor.AQUA + "Sets the fake max online player");
    }

    protected API getAPI(){
        return api;
    }

    protected void log(String string){
        logger.info(string);
    }
    public void createPlayer(String name) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), name);
        MinecraftServer server = MinecraftServer.getServer();
        WorldServer world = server.getWorldServer(0);
        PlayerInteractManager manager = new PlayerInteractManager(world);
        EntityPlayer player = new EntityPlayer(server, world, profile, manager);
      //  Bukkit.getLogger().info("Created player variables");

        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, player);
      //  Bukkit.getLogger().info("Created packet");

        for(Player aOnline : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) aOnline).getHandle().playerConnection.sendPacket(packet);
           // Bukkit.getLogger().info("Sent packet");
        }
    }
}
