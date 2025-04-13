package net.sideways_sky.realisticsleep;

import org.bukkit.Bukkit;
import org.bukkit.ServerTickManager;

public class Skipper {
    private final SkipMode skipMode;
    private final int skipSpeed;
    private float preSkipSpeed = 20;
    public boolean isSkipping = false;
    public void start(int ticks){
        if(skipMode == SkipMode.SPRINT) {
            bukkitManager.requestGameToSprint(ticks);
        } else {
            preSkipSpeed = bukkitManager.getTickRate();
            bukkitManager.setTickRate(skipSpeed);
            Bukkit.getScheduler().runTaskLater(RealisticSleep.instance, this::stop, ticks);
        }
        isSkipping = true;
    }
    public void stop(){
        if(skipMode == SkipMode.SPRINT) {
                Bukkit.getServerTickManager().stopSprinting();
        } else {
            bukkitManager.setTickRate(preSkipSpeed);
        }
        isSkipping = false;
    }
    private final ServerTickManager bukkitManager;
    public Skipper(SkipMode skipMode, int skipSpeed)  {
        this.skipMode = skipMode;
        this.skipSpeed = skipSpeed;
        bukkitManager = RealisticSleep.instance.getServer().getServerTickManager();

    }

}
