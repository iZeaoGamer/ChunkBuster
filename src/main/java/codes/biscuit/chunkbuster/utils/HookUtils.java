package codes.biscuit.chunkbuster.utils;

import codes.biscuit.chunkbuster.hooks.CoreProtectHook;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import codes.biscuit.chunkbuster.ChunkBuster;
import codes.biscuit.chunkbuster.hooks.FactionsUUIDHook;
import codes.biscuit.chunkbuster.hooks.MCoreFactionsHook;
import codes.biscuit.chunkbuster.hooks.WorldGuardHook;

public class HookUtils {

    private MCoreFactionsHook mCoreFactionsHook = null;
    private FactionsUUIDHook factionsUUIDHook = null;
    private WorldGuardHook worldGuardHook = null;
    private CoreProtectHook coreProtectHook = null;
    private HookType hook;
    private ChunkBuster main;

    /**
     * Construct the instance with the correct hook
     * @param hook The appropriate {@link HookType}
     */
    public HookUtils(HookType hook, ChunkBuster main) {
        this.hook = hook;
        this.main = main;
        if (hook == HookType.MCOREFACTIONS) {
            mCoreFactionsHook = new MCoreFactionsHook();
        } else if (hook == HookType.FACTIONSUUID) {
            factionsUUIDHook = new FactionsUUIDHook();
        } else if (hook == HookType.WORLDGUARD) {
            worldGuardHook = new WorldGuardHook();
        } else {
            this.hook = null;
        }
        if (main.getServer().getPluginManager().getPlugin("CoreProtect") != null) {
            this.coreProtectHook = new CoreProtectHook();
            main.getLogger().info("Hooked into CoreProtect");
        }
    }

    /**
     * Check if a player has a faction
     * <p>
     * This method isn't always applicable since they may not be using
     * a factions hook, so in that case it will return true (since
     * as false it will stop all the logic). Otherwise
     * it will check using the appropriate factions hook.
     * <p>
     * @param p The player to check
     * @return If this player has a faction
     */
    public boolean hasFaction(Player p) {
        if (hook == HookType.MCOREFACTIONS) {
            return mCoreFactionsHook.hasFaction(p);
        } else if (hook == HookType.FACTIONSUUID) {
            return factionsUUIDHook.hasFaction(p);
        } else {
            return true;
        }
    }

    /**
     * Check if a location is wilderness
     * <p>
     * This method isn't always applicable since they may not be using
     * a factions hook, so in that case it will return false. Otherwise
     * it will check using the appropriate factions hook.
     * <p>
     * @param loc The location to check
     * @return If this chunk is wilderness
     */
    public boolean isWilderness(Location loc) {
        if (hook == HookType.MCOREFACTIONS) {
            return mCoreFactionsHook.isWilderness(loc);
        } else if (hook == HookType.FACTIONSUUID) {
            return factionsUUIDHook.isWilderness(loc);
        } else {
            return false;
        }
    }

    /**
     * Compare a location to a player using the appropriate hook
     * <p>
     * This method is always applicable since even if a factions hook isn't being
     * used, then it will compare the region to WorldGuard flags.
     * <p>
     * @param loc The location to be compared
     * @param p The player to be compared
     * @return If the player can clear this chunk
     */
    public boolean compareLocToPlayer(Location loc, Player p) {
        if (hook == HookType.MCOREFACTIONS) {
            return mCoreFactionsHook.compareLocPlayerFaction(loc, p);
        } else if (hook == HookType.FACTIONSUUID) {
            return factionsUUIDHook.compareLocPlayerFaction(loc, p);
        } else {
            return worldGuardHook.checkLocationBreakFlag(loc, p);
        }
    }

    /**
     * Check if a player has the minimum role to place chunkbusters
     * <p>
     * This method isn't always applicable since they may not be using
     * a factions hook, so in that case it will return true. Otherwise
     * it will check using the appropriate factions hook.
     * <p>
     * @param p The player to check
     * @return If this player has the minimum role
     */
    public boolean checkRole(Player p) {
        if (hook == HookType.MCOREFACTIONS) {
            return mCoreFactionsHook.checkRole(p, main.getConfigValues().getMinimumRole());
        } else if (hook == HookType.FACTIONSUUID) {
            return factionsUUIDHook.checkRole(p, main.getConfigValues().getMinimumRole());
        } else {
            return true;
        }
    }

    /**
     * Whether CoreProtect is enabled or not
     */
    public boolean isCoreProtectEnabled() {
        return coreProtectHook != null && main.getConfigValues().logBlocks();
    }

    /**
     * Log a block as removed in CoreProtect
     * @param p The player to log
     * @param loc The block's location
     * @param mat The block's material
     * @param damage The block's damage value
     */
    public void logBlock(Player p, Location loc, Material mat, byte damage) {
        coreProtectHook.logBlock(p.getName(), loc, mat, damage);
    }

    public HookType getHookType() {
        return hook;
    }
}
