package krincraft.idk;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

public class IDKListener implements Listener {

    @EventHandler //定义为事件处理
    public void playerJoin(PlayerJoinEvent event) { //定义处理玩家加入事件
        Player player = event.getPlayer(); //获取事件对应的玩家
        event.setJoinMessage(player.getName() + " joined this server!\nMessage change by IDK plugin."); //修改加入消息为获取玩家名+指定字符串(\n为换行符)
    }

    @EventHandler //定义为事件处理
    public void playerLeave(PlayerQuitEvent event) { //定义处理玩家退出事件
        Player player = event.getPlayer(); //获取事件对应的玩家
        event.setQuitMessage(player.getName() + " leaved this server!\nMessage change by IDK plugin."); //修改退出消息为获取玩家名+指定字符串(\n为换行符)
    }

    @EventHandler //定义为事件处理
    public void click(InventoryClickEvent event) { //定义处理玩家背包点击事件
        Inventory inv = event.getInventory(); //获取事件对应的背包
        if(inv.getSize() == 9 && event.getRawSlot() == 0) { //如果背包大小为9格且点击的为第一格
            Player player = (Player) event.getWhoClicked(); //获取点击的玩家
            player.performCommand("idk help"); //使玩家执行指令
            player.closeInventory(); //使玩家关闭背包
            event.setCancelled(true); //取消点击事件(防止玩家将物品取出)
        } else if(inv.getSize() == 9) { //否则如果背包大小为9格
            event.setCancelled(true); //取消点击事件(防止玩家将物品取出)
        }
    }
}
