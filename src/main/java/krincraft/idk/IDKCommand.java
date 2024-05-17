package krincraft.idk;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class IDKCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) { //检测指令执行
        Configuration config = IDK.idk.getConfig();
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender; //获取执行玩家
            if(strings.length == 1 & strings[0].equals("reload")) {
                try{
                    IDK.idk.reloadConfig();
                    System.out.println("Config reloaded!");
                    player.sendMessage("Config reloaded!");
                } catch (Exception e) {
                    System.out.println("Config reload failed!");
                    player.sendMessage("Config reload failed!");
                    e.printStackTrace();
                    player.sendMessage(e.getStackTrace().toString());
                }
                return true;
            }
            if(strings.length == 2 && strings[0].equals("gm")) { //检测参数长度是否为二并且第一个参数是否为gm
                switch (strings[1]) { //检测第二个参数
                    case "0": //如果是0
                        player.setGameMode(GameMode.SURVIVAL); //设置玩家游戏模式为生存
                        player.sendMessage("You're now in SURVIVAL!"); //对玩家发送消息
                        break; //跳出循环
                    case "1": //如果是1
                        player.setGameMode(GameMode.CREATIVE); //设置玩家游戏模式为创造
                        player.sendMessage("You're now in CREATIVE!"); //对玩家发送消息
                        break; //跳出循环
                    case "2": //如果是2
                        player.setGameMode(GameMode.ADVENTURE); //设置玩家游戏模式为冒险
                        player.sendMessage("You're now in ADVENTURE!"); //对玩家发送消息
                        break; //跳出循环
                    case "3": //如果是3
                        player.setGameMode(GameMode.SPECTATOR); //设置玩家游戏模式为旁观
                        player.sendMessage("You're now in SPECTATOR!"); //对玩家发送消息
                        break; //跳出循环
                    default: //默认
                        player.sendMessage("Wrong Usage! Usage: /IDK gm <number of gamemodes>"); //对玩家发送消息
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
                other_meta.setDisplayName("Infomation"); //设置物品显示名称为Infomation
                List<String> lore = new ArrayList<>(); //创建一个列表，取名为lore，列表内元素类型为String
                lore.add("Author: MinecraftBaymax"); //列表中添加此字符串
                lore.add("Add by IDK plugin."); //列表中添加此字符串
                other_meta.setLore(lore); //设置物品lore
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
                player.sendMessage("IDK plugin help:" +
                        "\n/IDK help - This help message" +
                        "\n/IDK gm <number of gamemodes> - Change your gamemode" +
                        "\n/IDK open workbench - Open a chest or a workbench" +
                        "\n/IDK menu - open IDK menu" +
                        "\n/IDK ping - check your ping in game" +
                        "\n/IDK reload - reload config file"); //对玩家发送消息
                return true;
            }
            if(strings.length == 1 && strings[0].equals("ping")) {
                player.sendMessage("Your ping is: " + player.getPing() + "ms!"); //给玩家发送消息
                return true;
            }
        }
        return false;
    }
}
