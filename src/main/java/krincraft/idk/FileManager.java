package krincraft.idk;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.io.File;

public class FileManager {
    public boolean deleteDir(CommandSender sender, File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children == null)
                return false;
            for (String element : children) {
                File file = new File(dir, element);
                if (!deleteDir(file)) {
                    sender.sendMessage("Failed to delete "+ file.getAbsolutePath() + "!");
                } else {
                    sender.sendMessage("Succeed to delete "+ file.getAbsolutePath() + "!");
                }
            }
        }
        return dir.delete();
    }

    public boolean deleteDir(File dir) {
        return deleteDir((CommandSender) Bukkit.getConsoleSender(), dir);
    }
}
