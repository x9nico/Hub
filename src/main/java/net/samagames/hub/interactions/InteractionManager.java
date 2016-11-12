package net.samagames.hub.interactions;

import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.interactions.bumper.BumperManager;
import net.samagames.hub.interactions.graou.GraouManager;
import net.samagames.hub.interactions.meow.MeowManager;
import net.samagames.hub.interactions.sonicsquid.SonicSquidManager;
import net.samagames.hub.interactions.well.WellManager;
import net.samagames.hub.interactions.yodels.YodelsManager;
import org.bukkit.entity.Player;

import java.io.File;

public class InteractionManager extends AbstractManager
{
    private final YodelsManager yodelManager;
    private final SonicSquidManager sonicSquidManager;
    private final BumperManager bumperManager;
    private final MeowManager meowManager;
    private final GraouManager graouManager;
    private final WellManager wellManager;

    public InteractionManager(Hub hub)
    {
        super(hub);

        File interactionsDirectory = new File(hub.getDataFolder(), "interactions");

        if (!interactionsDirectory.exists())
            interactionsDirectory.mkdir();

        this.yodelManager = new YodelsManager(hub);
        this.sonicSquidManager = new SonicSquidManager(hub);
        this.bumperManager = new BumperManager(hub);
        this.meowManager = new MeowManager(hub);
        this.graouManager = new GraouManager(hub);
        this.wellManager = new WellManager(hub);
    }

    @Override
    public void onDisable()
    {
        this.yodelManager.onDisable();
        this.sonicSquidManager.onDisable();
        this.bumperManager.onDisable();
        this.meowManager.onDisable();
        this.graouManager.onDisable();
        this.wellManager.onDisable();
    }

    @Override
    public void onLogin(Player player)
    {
        this.yodelManager.onLogin(player);
        this.sonicSquidManager.onLogin(player);
        this.bumperManager.onLogin(player);
        this.meowManager.onLogin(player);
        this.graouManager.onLogin(player);
        this.wellManager.onLogin(player);
    }

    @Override
    public void onLogout(Player player)
    {
        this.yodelManager.onLogout(player);
        this.sonicSquidManager.onLogout(player);
        this.bumperManager.onLogout(player);
        this.meowManager.onLogout(player);
        this.graouManager.onLogout(player);
        this.wellManager.onLogout(player);
    }

    public boolean isInteracting(Player player)
    {
        if (this.yodelManager.hasPlayer(player))
            return true;
        else if (this.sonicSquidManager.hasPlayer(player))
            return true;
        else if (this.bumperManager.hasPlayer(player))
            return true;
        else if (this.meowManager.hasPlayer(player))
            return true;
        else if (this.graouManager.hasPlayer(player))
            return true;
        else if (this.wellManager.hasPlayer(player))
            return true;

        return false;
    }

    public GraouManager getGraouManager()
    {
        return this.graouManager;
    }

    public WellManager getWellManager()
    {
        return this.wellManager;
    }
}
