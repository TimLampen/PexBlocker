package me.timlampen.prisoncore;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class API{
    FakePlayers p;
    int fakeOnline = 0;
    public API(FakePlayers p){
        this.p = p;
    }

    public FileConfiguration getYaml(){
        return p.getConfig();
    }


    public int getRealOnline(){
        return Bukkit.getOnlinePlayers().size();
    }

    public int getFakeOnline(){
        return fakeOnline;
    }

    public int getMaxPlayers(){
        return p.getConfig().getInt("Fake players.Max players");
    }

    public void addFakeOnline(){
        fakeOnline++;
    }
    public void setOnline(int online){
        p.getConfig().set("Online", Integer.valueOf(online));
        p.saveConfig();
    }

    public void setMax(int max){
        p.getConfig().set("Max players", Integer.valueOf(max));
        p.saveConfig();
    }
    
}
