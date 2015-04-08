package me.tesnos6921.teleportals;

import java.util.Set;
import java.util.logging.Logger;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
 
 
public final class Main extends JavaPlugin implements Listener {
    
    public final Logger logger = java.util.logging.Logger
			.getLogger("Minecraft");
    public static Economy econ = null;
    
    @Override
    public void onEnable(){
    	this.logger.info("[TelePortals] " + "TelePortals Version 1.0 Has Been Enabled!");
        getServer().getPluginManager().registerEvents(this, this);
        saveConfig();
        if (!setupEconomy()) {
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
    }
    
    private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer()
				.getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}
 
    @Override
    public void onDisable() {
    	this.logger.info("[TelePortals] " + "TelePortals Version 1.0 Has Been Disabled!");
    	saveConfig();
    }
    
    @EventHandler
    public void onPlaceEvent(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.STONE_PLATE) {
            Block block2 = block.getRelative(0, -1, 0);
            Block block3 = block.getRelative(0, 2, 0);
            if (block2.getType() == Material.WOOL && block3.getType() == Material.WOOL && event.getPlayer().isOp() == true) {
            	event.getPlayer().sendMessage(ChatColor.GREEN + "TelePortal Created!  Now put a sign a side of the top block and input the info in the following format:\n" + "Location Name\n" + "Location X\n" + "Location Y\n" + "Location Z\n" + "Then look at the sign and type /rtp to register the TelePortal!\n");
                block2.setType(Material.OBSIDIAN);
                block3.setType(Material.OBSIDIAN);
            } else if (block2.getType() == Material.WOOL && block3.getType() == Material.WOOL && event.getPlayer().isOp() != true) {
            	event.getPlayer().sendMessage(ChatColor.RED + "Only Operators can create TelePortals!");
            }
        }
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
    	Player p = e.getPlayer();
    	Block block = p.getLocation().getBlock().getRelative(1, 2, 0);
    	if(block.getType().toString().equals("WALL_SIGN")) {
    		Sign sign = (Sign) block.getState();
    		if(ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[TelePortal]")) {
    			if(getConfig().contains("teleportals." + sign.getLine(1) + ".x")) {
    				if(getConfig().contains("teleportals." + sign.getLine(1) + ".cost")) {
	    				EconomyResponse r = econ.withdrawPlayer(p.getName(), Integer.parseInt(getConfig().getString("teleportals." + sign.getLine(1) + ".cost")));
	    				EconomyResponse r2 = econ.depositPlayer(getConfig().getString("teleportals." + sign.getLine(1) + ".owner"), Integer.parseInt(getConfig().getString("teleportals." + sign.getLine(1) + ".cost")));
	    				if (r.transactionSuccess() && r2.transactionSuccess()) {
	    					Location tplocation = new Location((World) p.getWorld(), Double.parseDouble(getConfig().getString("teleportals." + sign.getLine(1) + ".x")), Double.parseDouble(getConfig().getString("teleportals." + sign.getLine(1) + ".y")), Double.parseDouble(getConfig().getString("teleportals." + sign.getLine(1) + ".z")));
	    		            if (p.getLocation().getBlock().getRelative(0, 0, 0).getType() == Material.STONE_PLATE) {
	    		                p.teleport(tplocation);
	    		                p.sendMessage(ChatColor.GREEN + "You have been teleported to " + sign.getLine(1) + "!");
	    		            }
	    				} else {
	    					p.sendMessage(ChatColor.RED
	    							+ "You don't have enough money to use this TelePortal!");
	    				}
    				} else {
    					Location tplocation = new Location((World) p.getWorld(), Double.parseDouble(getConfig().getString("teleportals." + sign.getLine(1) + ".x")), Double.parseDouble(getConfig().getString("teleportals." + sign.getLine(1) + ".y")), Double.parseDouble(getConfig().getString("teleportals." + sign.getLine(1) + ".z")));
    		            if (p.getLocation().getBlock().getRelative(0, 0, 0).getType() == Material.STONE_PLATE) {
    		                p.teleport(tplocation);
    		                p.sendMessage(ChatColor.GREEN + "You have been teleported to " + sign.getLine(1) + "!");
    		            }
    				}
    			} else {
    				p.sendMessage(ChatColor.RED + "Please contact the server admin because this TelePortal seems to be broken!");
    			}
    		}
    	} else if(!block.getType().toString().equals("WALL_SIGN")) {
    		Block block2 = p.getLocation().getBlock().getRelative(-1, 2, 0);
        	if(block2.getType().toString().equals("WALL_SIGN")) {
        		Sign sign = (Sign) block2.getState();
        		if(ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[TelePortal]")) {
        			if(getConfig().contains("teleportals." + sign.getLine(1) + ".x")) {
        				if(getConfig().contains("teleportals." + sign.getLine(1) + ".cost")) {
    	    				EconomyResponse r = econ.withdrawPlayer(p.getName(), Integer.parseInt(getConfig().getString("teleportals." + sign.getLine(1) + ".cost")));
    	    				EconomyResponse r2 = econ.depositPlayer(getConfig().getString("teleportals." + sign.getLine(1) + ".owner"), Integer.parseInt(getConfig().getString("teleportals." + sign.getLine(1) + ".cost")));
    	    				if (r.transactionSuccess() && r2.transactionSuccess()) {
    	    					Location tplocation = new Location((World) p.getWorld(), Double.parseDouble(getConfig().getString("teleportals." + sign.getLine(1) + ".x")), Double.parseDouble(getConfig().getString("teleportals." + sign.getLine(1) + ".y")), Double.parseDouble(getConfig().getString("teleportals." + sign.getLine(1) + ".z")));
    	    		            if (p.getLocation().getBlock().getRelative(0, 0, 0).getType() == Material.STONE_PLATE) {
    	    		                p.teleport(tplocation);
    	    		                p.sendMessage(ChatColor.GREEN + "You have been teleported to " + sign.getLine(1) + "!");
    	    		            }
    	    				} else {
    	    					p.sendMessage(ChatColor.RED
    	    							+ "You don't have enough money to use this TelePortal!");
    	    				}
        				} else {
        					Location tplocation = new Location((World) p.getWorld(), Double.parseDouble(getConfig().getString("teleportals." + sign.getLine(1) + ".x")), Double.parseDouble(getConfig().getString("teleportals." + sign.getLine(1) + ".y")), Double.parseDouble(getConfig().getString("teleportals." + sign.getLine(1) + ".z")));
        		            if (p.getLocation().getBlock().getRelative(0, 0, 0).getType() == Material.STONE_PLATE) {
        		                p.teleport(tplocation);
        		                p.sendMessage(ChatColor.GREEN + "You have been teleported to " + sign.getLine(1) + "!");
        		            }
        				}
        			} else {
        				p.sendMessage(ChatColor.RED + "Please contact the server admin because this TelePortal seems to be broken!");
        			}
        		}
        	} else if(!block2.getType().toString().equals("WALL_SIGN")) {
        		Block block3 = p.getLocation().getBlock().getRelative(0, 2, 1);
            	if(block3.getType().toString().equals("WALL_SIGN")) {
            		Sign sign = (Sign) block3.getState();
            		if(ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[TelePortal]")) {
            			if(getConfig().contains("teleportals." + sign.getLine(1) + ".x")) {
            				if(getConfig().contains("teleportals." + sign.getLine(1) + ".cost")) {
        	    				EconomyResponse r = econ.withdrawPlayer(p.getName(), Integer.parseInt(getConfig().getString("teleportals." + sign.getLine(1) + ".cost")));
        	    				EconomyResponse r2 = econ.depositPlayer(getConfig().getString("teleportals." + sign.getLine(1) + ".owner"), Integer.parseInt(getConfig().getString("teleportals." + sign.getLine(1) + ".cost")));
        	    				if (r.transactionSuccess() && r2.transactionSuccess()) {
        	    					Location tplocation = new Location((World) p.getWorld(), Double.parseDouble(getConfig().getString("teleportals." + sign.getLine(1) + ".x")), Double.parseDouble(getConfig().getString("teleportals." + sign.getLine(1) + ".y")), Double.parseDouble(getConfig().getString("teleportals." + sign.getLine(1) + ".z")));
        	    		            if (p.getLocation().getBlock().getRelative(0, 0, 0).getType() == Material.STONE_PLATE) {
        	    		                p.teleport(tplocation);
        	    		                p.sendMessage(ChatColor.GREEN + "You have been teleported to " + sign.getLine(1) + "!");
        	    		            }
        	    				} else {
        	    					p.sendMessage(ChatColor.RED
        	    							+ "You don't have enough money to use this TelePortal!");
        	    				}
            				} else {
            					Location tplocation = new Location((World) p.getWorld(), Double.parseDouble(getConfig().getString("teleportals." + sign.getLine(1) + ".x")), Double.parseDouble(getConfig().getString("teleportals." + sign.getLine(1) + ".y")), Double.parseDouble(getConfig().getString("teleportals." + sign.getLine(1) + ".z")));
            		            if (p.getLocation().getBlock().getRelative(0, 0, 0).getType() == Material.STONE_PLATE) {
            		                p.teleport(tplocation);
            		                p.sendMessage(ChatColor.GREEN + "You have been teleported to " + sign.getLine(1) + "!");
            		            }
            				}
            			} else {
            				p.sendMessage(ChatColor.RED + "Please contact the server admin because this TelePortal seems to be broken!");
            			}
            		}
            	} else if(!block3.getType().toString().equals("WALL_SIGN")) {
            		Block block4 = p.getLocation().getBlock().getRelative(0, 2, 1);
                	if(block4.getType().toString().equals("WALL_SIGN")) {
                		Sign sign = (Sign) block4.getState();
                		if(ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[TelePortal]")) {
                			if(getConfig().contains("teleportals." + sign.getLine(1) + ".x")) {
                				if(getConfig().contains("teleportals." + sign.getLine(1) + ".cost")) {
            	    				EconomyResponse r = econ.withdrawPlayer(p.getName(), Integer.parseInt(getConfig().getString("teleportals." + sign.getLine(1) + ".cost")));
            	    				EconomyResponse r2 = econ.depositPlayer(getConfig().getString("teleportals." + sign.getLine(1) + ".owner"), Integer.parseInt(getConfig().getString("teleportals." + sign.getLine(1) + ".cost")));
            	    				if (r.transactionSuccess() && r2.transactionSuccess()) {
            	    					Location tplocation = new Location((World) p.getWorld(), Double.parseDouble(getConfig().getString("teleportals." + sign.getLine(1) + ".x")), Double.parseDouble(getConfig().getString("teleportals." + sign.getLine(1) + ".y")), Double.parseDouble(getConfig().getString("teleportals." + sign.getLine(1) + ".z")));
            	    		            if (p.getLocation().getBlock().getRelative(0, 0, 0).getType() == Material.STONE_PLATE) {
            	    		                p.teleport(tplocation);
            	    		                p.sendMessage(ChatColor.GREEN + "You have been teleported to " + sign.getLine(1) + "!");
            	    		            }
            	    				} else {
            	    					p.sendMessage(ChatColor.RED
            	    							+ "You don't have enough money to use this TelePortal!");
            	    				}
                				} else {
                					Location tplocation = new Location((World) p.getWorld(), Double.parseDouble(getConfig().getString("teleportals." + sign.getLine(1) + ".x")), Double.parseDouble(getConfig().getString("teleportals." + sign.getLine(1) + ".y")), Double.parseDouble(getConfig().getString("teleportals." + sign.getLine(1) + ".z")));
                		            if (p.getLocation().getBlock().getRelative(0, 0, 0).getType() == Material.STONE_PLATE) {
                		                p.teleport(tplocation);
                		                p.sendMessage(ChatColor.GREEN + "You have been teleported to " + sign.getLine(1) + "!");
                		            }
                				}
                			} else {
                				p.sendMessage(ChatColor.RED + "Please contact the server admin because this TelePortal seems to be broken!");
                			}
                		}
                	}
            	}
        	}
    	}
    }
    
    public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
        if(commandLabel.equalsIgnoreCase("rtp")){
	    	Player p = (Player) sender;
	        Block block = p.getTargetBlock((Set<Material>)null, 100);
	        if (block.getType().toString().equals("WALL_SIGN")) {
		        Sign sign = (Sign) block.getState();
		        sign.update();
		        if(!getConfig().contains("teleportals." + sign.getLine(0) + ".x")){
	        		getConfig().set("teleportals." + sign.getLine(0) + ".x", sign.getLine(1));
	        		getConfig().set("teleportals." + sign.getLine(0) + ".y", sign.getLine(2));
	        		getConfig().set("teleportals." + sign.getLine(0) + ".z", sign.getLine(3));
	        		getConfig().set("teleportals." + sign.getLine(0) + ".owner", p.getName());
	        		if(args.length == 1){
	        			getConfig().set("teleportals." + sign.getLine(0) + ".cost", args[0]);
	        		}
	        		saveConfig();
	        		sign.setLine(1, sign.getLine(0));
	        		sign.setLine(0, ChatColor.AQUA +"[TelePortal]");
	        		sign.setLine(2, "");
	        		sign.setLine(3, "");
	        		sign.update();
	        		p.sendMessage("TelePortal Registered!");
	        		return true;
	        	} else {
	        		p.sendMessage("This TelePortal already exists or there was an error!");
	        		return false;
	        	}
	        } else {
	        	p.sendMessage("Not a sign!");
	        	return false;
	        }
        }
        return false;
    }
}