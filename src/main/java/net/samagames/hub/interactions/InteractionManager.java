package net.samagames.hub.interactions;

import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.interactions.bumper.BumperManager;
import net.samagames.hub.interactions.magicchests.MagicChestManager;
import net.samagames.hub.interactions.sonicsquid.SonicSquidManager;
import net.samagames.hub.interactions.yodels.YodelsManager;
import org.bukkit.entity.Player;

import java.io.File;

public class InteractionManager extends AbstractManager
{
    private final YodelsManager yodelManager;
    private final SonicSquidManager sonicSquidManager;
    private final BumperManager bumperManager;
    private final MagicChestManager magicChestManager;

    public InteractionManager(Hub hub)
    {
        super(hub);

        File interactionsDirectory = new File(hub.getDataFolder(), "interactions");

        if (!interactionsDirectory.exists())
            interactionsDirectory.mkdir();

        this.yodelManager = new YodelsManager(hub);
        this.sonicSquidManager = new SonicSquidManager(hub);
        this.bumperManager = new BumperManager(hub);
        this.magicChestManager = new MagicChestManager(hub);
    }

    @Override
    public void onDisable()
    {
        this.yodelManager.onDisable();
        this.sonicSquidManager.onDisable();
        this.bumperManager.onDisable();
        this.magicChestManager.onDisable();
    }

    @Override
    public void onLogin(Player player)
    {
        this.yodelManager.onLogin(player);
        this.sonicSquidManager.onLogin(player);
        this.bumperManager.onLogin(player);
        this.magicChestManager.onLogin(player);
    }

    @Override
    public void onLogout(Player player)
    {
        this.yodelManager.onLogout(player);
        this.sonicSquidManager.onLogout(player);
        this.bumperManager.onLogout(player);
        this.magicChestManager.onLogin(player);
    }

    public boolean isInteracting(Player player)
    {
        return this.yodelManager.hasPlayer(player) || this.sonicSquidManager.hasPlayer(player) || this.bumperManager.hasPlayer(player) || this.magicChestManager.hasPlayer(player);
    }
}