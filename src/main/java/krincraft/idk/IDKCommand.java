package krincraft.idk;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class IDKCommand implements CommandExecutor {
    static String filename = "messages.yml";

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) { //检测指令执行
        IDKMessageConfig messages = new IDKMessageConfig(IDK.idk, filename) {
            @Override
            public void save() {
                super.save();
            }
        };
        Configuration config = IDK.idk.getConfig();
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender; //获取执行玩家
            if(strings.length == 1 & strings[0].equals("reload")) {
                try{
                    messages.save();
                    IDK.idk.check();
                    IDK.idk.reloadConfig();
                    messages.reload(filename);
                    // 测试是否有空值
                    config.getString("IDK_Helper_name");
                    config.getStringList("IDK_Helper_lore");
                    config.getString("IDK_Workbench_name");
                    config.getStringList("IDK_Workbench_lore");
                    config.getString("menu_title");
                    config.getString("workbench_title");
                    messages.getString("failed");
                    messages.getString("failed_p");
                    messages.getString("survival");
                    messages.getString("creative");
                    messages.getString("adventure");
                    messages.getString("spectator");
                    System.out.println(messages.getString("reload"));
                    player.sendMessage(messages.getString("reload"));
                } catch (Exception e) {
                    System.out.println(messages.getString("failed"));
                    player.sendMessage(messages.getString("failed_p"));
                    e.printStackTrace();
                }
                return true;
            }
            if(strings.length == 2 && strings[0].equals("gm")) { //检测参数长度是否为二并且第一个参数是否为gm
                switch (strings[1]) { //检测第二个参数
                    case "0": //如果是0
                        player.setGameMode(GameMode.SURVIVAL); //设置玩家游戏模式为生存
                        player.sendMessage(messages.getString("survival")); //对玩家发送消息
                        break; //跳出循环
                    case "1": //如果是1
                        player.setGameMode(GameMode.CREATIVE); //设置玩家游戏模式为创造
                        player.sendMessage(messages.getString("creative")); //对玩家发送消息
                        break; //跳出循环
                    case "2": //如果是2
                        player.setGameMode(GameMode.ADVENTURE); //设置玩家游戏模式为冒险
                        player.sendMessage(messages.getString("adventure")); //对玩家发送消息
                        break; //跳出循环
                    case "3": //如果是3
                        player.setGameMode(GameMode.SPECTATOR); //设置玩家游戏模式为旁观
                        player.sendMessage(messages.getString("spectator")); //对玩家发送消息
                        break; //跳出循环
                    default: //默认
                        break; //跳出循环
                }
                return true;
            }
            if(strings[0].equals("menu")) {
                Inventory inv = Bukkit.createInventory(null, 9, config.getString("menu_title")); //创建一个大小为9格(必须为9或9的倍数)的背包，标题设置为IDK Chest
                int left = inv.getSize(); //获取背包大小并赋值到left上
                int slot = 0; //设置slot的值为0
                int i = 0;
                List<String> slot_used = new ArrayList<>();

                ItemStack idk_helper = new ItemStack(Material.DIAMOND, 1); //创建一个物品，材质为钻石，数量为1个
                ItemMeta idk_helper_meta = idk_helper.getItemMeta(); //获取物品meta
                idk_helper_meta.setDisplayName(config.getString("IDK_Helper_name"));
                idk_helper_meta.setLore(config.getStringList("IDK_Helper_lore"));
                idk_helper.setItemMeta(idk_helper_meta); //设置物品meta
                inv.setItem(1, idk_helper); //添加物品到背包中

                left -= 1; //left等于left减1
                slot_used.add("1");

                ItemStack idk_workbench = new ItemStack(Material.LEGACY_WORKBENCH, 1);
                ItemMeta idk_workbench_meta = idk_workbench.getItemMeta();
                idk_workbench_meta.setDisplayName(config.getString("IDK_Workbench_name"));
                idk_workbench_meta.setLore(config.getStringList("IDK_Workbench_lore"));
                idk_workbench.setItemMeta(idk_workbench_meta);
                inv.setItem(7, idk_workbench);

                left -= 1;
                slot_used.add("7");

                ItemStack other = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1); //创建一个物品，材质为蓝色玻璃板，数量为1个
                ItemMeta other_meta = other.getItemMeta(); //获取物品meta
                other_meta.setDisplayName(config.getString("Information_name")); //设置物品显示名称为Infomation
                other_meta.setLore(config.getStringList("Information_lore"));
                other.setItemMeta(other_meta); //设置物品meta

                while(left > 0) { //如果left大于0则循环
                    inv.setItem(slot, other); //设置背包中第slot位的物品为other(默认第一格的槽位ID为0)
                    left -= 1; //left等于left减1
                    slot += 1; //slot等于slot加1
                    if(String.valueOf(slot).equals(slot_used.get(i))) {
                        slot += 1;
                        try {
                            if(!slot_used.get(i+1).isEmpty()) {
                                i += 1;
                            }
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                    }
                }

                player.openInventory(inv); //使指定玩家打开背包
                return true;
            }
            if(strings.length == 2 && strings[0].equals("open")) { //检测参数长度是否为二并且第一个参数是否为open
                if(strings[1].equals("workbench")) { //检测第二个参数是否为workbench
                    Inventory inv = Bukkit.createInventory(null, InventoryType.WORKBENCH, config.getString("workbench_title")); //创建一个背包，标题为IDK Workbench，类型为工作台
                    player.openInventory(inv); //使指定玩家打开背包
                    return true;
                }
            }
            if(strings.length == 1 && strings[0].equals("help")) { //检测参数长度是否为1并且第一个参数是否为help
                List<String> help_msg = config.getStringList("Help");
                String help_msg_string = help_msg.toString().replace("[", "");
                String help_msg_fix = help_msg_string.replace("]", "");
                String help_msg_fix2 = help_msg_fix.replace(",", "");
                player.sendMessage(help_msg_fix2); //对玩家发送消息
                return true;
            }
            if(strings.length == 1 && strings[0].equals("ping")) {
                player.sendMessage(messages.getString("ping") + player.getPing() + "ms!"); //给玩家发送消息
                return true;
            }
        } else{
            System.out.println(messages.getString("no-console"));
        }
        return false;
    }
}
