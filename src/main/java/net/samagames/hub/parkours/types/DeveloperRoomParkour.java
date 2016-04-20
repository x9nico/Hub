package net.samagames.hub.parkours.types;

import net.samagames.hub.Hub;
import net.samagames.hub.parkours.Parkour;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeveloperRoomParkour extends Parkour implements Listener
{
    private static final long MUSIC_LENGTH = 20L * 175;

    private final Pair<Location, Location> portals;
    private final List<UUID> expected;
    private final Location minusFloor;
    private final String resourcePack;

    public DeveloperRoomParkour(Hub hub, Location spawn, Location end, Location fail, Pair<Location, Location> portals, Location minusFloor, String resourcePack)
    {
        super(hub, "Salle de test n°42", "de la", "la", spawn, end, fail, 0, new ArrayList<>(), 5, null);

        this.expected = new ArrayList<>();

        this.portals = portals;
        this.minusFloor = minusFloor;
        this.resourcePack = resourcePack;

        hub.getServer().getPluginManager().registerEvents(this, hub);
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event)
    {
        if (!this.isParkouring(event.getPlayer().getUniqueId()))
            return;

        if (event.getFrom().distanceSquared(this.portals.getKey()) < 16)
            event.getPlayer().teleport(this.portals.getValue().clone().subtract(0.0D, 1.0D, 0.0D));
        else if (event.getFrom().distanceSquared(this.portals.getValue()) < 16)
            event.getPlayer().teleport(this.portals.getKey().clone().subtract(0.0D, 1.0D, 0.0D));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (!this.isParkouring(event.getPlayer().getUniqueId()))
            return;

        event.setCancelled(true);

        if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.IRON_PLATE && event.getClickedBlock().getLocation().equals(this.minusFloor))
        {
            event.getPlayer().teleport(this.fail);
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_PISTON_CONTRACT, 1.0F, 1.0F);
        }
    }

    @EventHandler
    public void onPlayerResourcePackStatus(PlayerResourcePackStatusEvent event)
    {
        if (!this.expected.contains(event.getPlayer().getUniqueId()) || event.getStatus() != PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED)
            return;

        this.playMusic(event.getPlayer());

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (expected.contains(event.getPlayer().getUniqueId()))
                    playMusic(event.getPlayer());
                else
                    this.cancel();
            }
        }.runTaskTimer(this.hub, MUSIC_LENGTH, MUSIC_LENGTH);
    }

    @Override
    public void addPlayer(Player player)
    {
        super.addPlayer(player);

        this.expected.add(player.getUniqueId());
        player.setResourcePack("http://resources.samagames.net/" + this.resourcePack + ".zip");
    }

    @Override
    public void removePlayer(Player player)
    {
        super.removePlayer(player);

        this.expected.remove(player.getUniqueId());
        player.setResourcePack("http://resources.samagames.net/samareset.zip");
        player.playEffect(player.getLocation(), Effect.RECORD_PLAY, 0);
    }

    private void playMusic(Player player)
    {
        player.playEffect(player.getLocation(), Effect.RECORD_PLAY, Material.GREEN_RECORD.getId());
    }
}