package idk.team;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class IDKListener implements Listener {
    String prefix = "";
    private void reload() {
        this.prefix = IDK.idk.prefix;
    }

    @EventHandler //定义为事件处理
    public void playerJoin(PlayerJoinEvent event) { //定义处理玩家加入事件
        reload();
        FileConfiguration config = IDK.idk.getConfig();
        IDKMessageConfig messages = new IDKMessageConfig(IDK.idk.data_folder, config.getString("lang")) {
            protected void finalize() throws Throwable {
                super.finalize();
            }
        };
        Player player = event.getPlayer(); //获取事件对应的玩家
        List join_message = messages.getStringList("join_message");
        Object[] join_message_fix = join_message.toArray();
        StringBuilder fixed = new StringBuilder();
        for(int i = 0; i < join_message_fix.length; i++) {
            fixed.append(join_message_fix[i].toString());
            if (i != join_message_fix.length - 1) {
                fixed.append("\n");
            }
        }
        event.setJoinMessage(prefix+fixed.toString().replace("[Player]", player.getName())); //修改加入消息为获取玩家名+指定字符串(\n为换行符)
        String warning_title = messages.getString("warning_title");
        String warning_test = messages.getString("warning_test");
        String warning_beta = messages.getString("warning_beta");
        String warning_alpha = messages.getString("warning_alpha");
        String warning_2 = messages.getString("warning_2");
        if (config.getBoolean("test-notify")) {
            if (IDK.idk.test_build) {
                player.sendTitle(prefix+warning_title, prefix+warning_test);
                Timer timer = new Timer();
                TimerTask use_at_own_risks = new TimerTask() {
                    @Override
                    public void run() {
                        player.sendTitle(prefix+warning_title, prefix+warning_2);
                    }
                };
                timer.schedule(use_at_own_risks,5000);
            }
            else if (IDK.idk.alpha_build) {
                player.sendTitle(prefix+warning_title, warning_alpha);
                Timer timer = new Timer();
                TimerTask use_at_own_risks = new TimerTask() {
                    @Override
                    public void run() {
                        player.sendTitle(prefix+warning_title, warning_2);
                    }
                };
                timer.schedule(use_at_own_risks,5000);
            }
            else if (IDK.idk.beta_build) {
                player.sendTitle(prefix+warning_title, warning_beta);
                Timer timer = new Timer();
                TimerTask use_at_own_risks = new TimerTask() {
                    @Override
                    public void run() {
                        player.sendTitle(prefix+warning_title, warning_2);
                    }
                };
                timer.schedule(use_at_own_risks,5000);
            }
        }
    }

    @EventHandler //定义为事件处理
    public void playerLeave(PlayerQuitEvent event) { //定义处理玩家退出事件
        reload();
        FileConfiguration config = IDK.idk.getConfig();
        IDKMessageConfig messages = new IDKMessageConfig(IDK.idk.data_folder, config.getString("lang")) {
            protected void finalize() throws Throwable {
                super.finalize();
            }
        };
        Player player = event.getPlayer(); //获取事件对应的玩家
        List leave_message = messages.getStringList("leave_message");
        Object[] leave_message_fix = leave_message.toArray();
        StringBuilder fixed = new StringBuilder();
        for(int i = 0; i < leave_message_fix.length; i++) {
            fixed.append(leave_message_fix[i].toString());
            if (i != leave_message_fix.length - 1) {
                fixed.append("\n");
            }
        }
        event.setQuitMessage(prefix+fixed.toString().replace("[Player]", player.getName())); //修改退出消息为获取玩家名+指定字符串(\n为换行符)
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
