package net.samagames.hub.stats;

import net.samagames.api.SamaGamesAPI;
import net.samagames.core.api.stats.Leaderboard;
import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.DisplayedStat;
import org.bukkit.Bukkit;

import java.util.HashMap;

public class StatsManager extends AbstractManager
{
    private HashMap<String, Leaderboard> leaderboards;

    public StatsManager(Hub hub)
    {
        super(hub);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this.hub, this::reloadStats, 20L * 120, 20L * 120);

        this.reloadStats();
    }

    public void reloadStats()
    {
        for(AbstractGame game : this.hub.getGameManager().getGames().values())
        {
            for(DisplayedStat stat : game.getDisplayedStats())
            {
                if(this.leaderboards.containsKey(stat.getDatabaseName()))
                    this.leaderboards.remove(stat.getDatabaseName());

                this.leaderboards.put(stat.getDatabaseName(), SamaGamesAPI.get().getStatsManager(game.getCodeName()).getLeaderboard(stat.getDatabaseName()));
            }
        }
    }

    public Leaderboard getLeaderbordOf(String stat)
    {
        if(this.leaderboards.containsKey(stat))
            return this.leaderboards.get(stat);
        else
            return null;
    }

    @Override
    public String getName()
    {
        return "StatsManager";
    }
}
