package net.sideways_sky.realisticsleep;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.List;
public class SleepingWorld {
//    private static final int NightStart = 12545;
    private static final int NightEnd = 23500;
    private static final int NightDir = 10955;
    public final World world;
    public SleepingWorld(World world) {
        this.world = world;
    }
    private int numSleepersNeeded;
    private List<Player> sleepers;
    private int numInBedNotSleep;
    private TextComponent getPlayersSleepText(){
        return Component.text(sleepers.size() + "/" + numSleepersNeeded + " players sleeping" +
                (numInBedNotSleep > 0 ? " (" + numInBedNotSleep + " going to sleep)" : "") +
                (RealisticSleep.instance.skipper.isSkipping ? " " + Math.round(((float) ((world.getTime() - NightEnd) * 100) / NightDir) + 100) + "%" : ""));
    }
    public void update(){
        var a = world.getGameRuleValue(GameRule.PLAYERS_SLEEPING_PERCENTAGE) * world.getPlayers().size();
        numSleepersNeeded = Math.max(a / 100, 1);
        sleepers = world.getPlayers().stream().filter(HumanEntity::isDeeplySleeping).toList();
        numInBedNotSleep = (int) world.getPlayers().stream().filter(player -> player.isSleeping() && !player.isDeeplySleeping()).count();
        boolean gotNeededSleepers = (sleepers.size()*100) / a >= 1;

        if(RealisticSleep.instance.skipper.isSkipping && !gotNeededSleepers){
            RealisticSleep.instance.skipper.stop();
        } else if(!RealisticSleep.instance.skipper.isSkipping && gotNeededSleepers){
            RealisticSleep.instance.skipper.start((int) (NightEnd - world.getTime()));
        }
    }
    public void sendSleepingUpdate(){
        world.getPlayers().forEach(player -> player.sendActionBar(getPlayersSleepText()));
    }


}
