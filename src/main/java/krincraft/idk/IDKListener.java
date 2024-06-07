package krincraft.idk;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import java.util.Timer;
import java.util.TimerTask;

public class IDKListener implements Listener {
    @EventHandler //定义为事件处理
    public void playerJoin(PlayerJoinEvent event) { //定义处理玩家加入事件
        Player player = event.getPlayer(); //获取事件对应的玩家
        event.setJoinMessage(player.getName() + " 加入了本服务器"); //修改加入消息为获取玩家名+指定字符串(\n为换行符)
        if (IDK.idk.test_build) {
            player.sendTitle("警告！", "这是一个测试版插件！！");
            Timer timer = new Timer();
            TimerTask use_at_own_risks = new TimerTask() {
                @Override
                public void run() {
                    player.sendTitle("警告！", "请自行承担风险使用！");
                }
            };
            timer.schedule(use_at_own_risks,5000);
        } else if (IDK.idk.beta_build) {
            player.sendTitle("警告！", "这是一个Beta版插件！！");
            Timer timer = new Timer();
            TimerTask use_at_own_risks = new TimerTask() {
                @Override
                public void run() {
                    player.sendTitle("警告!", "请自行承担风险使用！");
                }
            };
            timer.schedule(use_at_own_risks,5000);
        }
    }

    @EventHandler //定义为事件处理
    public void playerLeave(PlayerQuitEvent event) { //定义处理玩家退出事件
        Player player = event.getPlayer(); //获取事件对应的玩家
        event.setQuitMessage(player.getName() + " 离开了本服务器"); //修改退出消息为获取玩家名+指定字符串(\n为换行符)
    }

    @EventHandler //定义为事件处理
    public void click(InventoryClickEvent event) { //定义处理玩家背包点击事件
        Inventory inv = event.getInventory(); //获取事件对应的背包
        if(inv.getSize() == 9 && event.getRawSlot() == 1) { //如果背包大小为9格且点击的为第一格
            Player player = (Player) event.getWhoClicked(); //获取点击的玩家
            player.closeInventory(); //使玩家关闭背包
            player.performCommand("idk help"); //使玩家执行指令
            event.setCancelled(true); //取消点击事件(防止玩家将物品取出)
        } else if(inv.getSize() == 9 && event.getRawSlot() == 7) { //否则如果背包大小为9格
            Player player = (Player) event.getWhoClicked(); //获取点击的玩家
            player.closeInventory(); //使玩家关闭背包
            player.performCommand("idk open workbench"); //使玩家执行指令
            event.setCancelled(true); //取消点击事件(防止玩家将物品取出)
        } else if(inv.getSize() == 9) { //否则如果背包大小为9格
            event.setCancelled(true); //取消点击事件(防止玩家将物品取出)
        }
    }
}
