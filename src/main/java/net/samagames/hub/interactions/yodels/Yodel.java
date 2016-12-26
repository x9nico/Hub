package net.samagames.hub.interactions.yodels;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteraction;
import net.samagames.tools.ProximityUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class Yodel extends AbstractInteraction
{
    private Location boarding;
    private Location start;
    private Location end;
    private Location landing;
    private boolean reverse;

    private double length;

    private final BukkitTask startTask;
    private final ArmorStand startBeacon;
    private static final Map<UUID, YodelRunner> runnerList = new HashMap<>();

    Yodel(Hub hub, Location boarding, Location start, Location end, Location landing, boolean reverse)
    {
        super(hub);
        this.boarding = boarding.clone();
        this.start    = start.clone();
        this.end      = end.clone();
        this.landing  = landing.clone();
        this.reverse = reverse;

        this.length = start.distanceSquared(end);

        this.startBeacon = boarding.getWorld().spawn(reverse ? landing : boarding, ArmorStand.class);
        this.startBeacon.setVisible(false);
        this.startBeacon.setGravity(false);

        this.hub.getTaskManager().getCirclesTask().addCircleAt(reverse ? landing : boarding);
        this.startTask = ProximityUtils.onNearbyOf(this.hub, this.startBeacon, 0.5D, 0.5D, 0.5D, Player.class, this::play);
    }

    public Location getBoarding()
    {
        return boarding.clone();
    }

    public Location getStart()
    {
        return start.clone();
    }

    public Location getEnd()
    {
        return end.clone();
    }

    public Location getLanding()
    {
        return landing.clone();
    }

    public Vector getAngleVector()
    {
        return end.toVector().subtract(start.toVector());
    }

    public double getLength()
    {
        return length;
    }

    @Override
    public void play(Player player)
    {
        if (runnerList.containsKey(player.getUniqueId()) || player.getGameMode() == GameMode.SPECTATOR)
            return;

        YodelRunner runner = new YodelRunner(this.hub, this, player, this.reverse);
        runnerList.put(player.getUniqueId(), runner);
        runner.start();

        this.hub.getServer().getScheduler().runTask(this.hub, () -> SamaGamesAPI.get().getAchievementManager().getAchievementByID(58).unlock(player.getUniqueId()));
    }

    @Override
    public boolean hasPlayer(Player player)
    {
        return runnerList.containsKey(player.getUniqueId());
    }

    @Override
    public void onDisable()
    {
        runnerList.values().forEach(YodelRunner::stop);

        this.startTask.cancel();
        this.startBeacon.remove();
    }

    @Override
    public void stop(Player player)
    {
        YodelRunner runner = runnerList.get(player.getUniqueId());

        if (runner != null)
        {
            runner.stop();
            this.hub.getServer().getScheduler().runTaskLater(this.hub, () -> runnerList.remove(player.getUniqueId()), 40L);
        }
    }
}
