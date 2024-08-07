package idk.team;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class FileManager {
    public boolean deleteDir(CommandSender sender, File dir) {
        String prefix = IDK.idk.prefix;
        FileConfiguration config = IDK.idk.getConfig();
        IDKMessageConfig messages = new IDKMessageConfig(IDK.idk.data_folder, config.getString("lang")) {
            protected void finalize() throws Throwable {
                super.finalize();
            }
        };
        String del_fail = messages.getString("del_fail");
        String del_complete = messages.getString("del_complete");
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children == null)
                return false;
            for (String element : children) {
                File file = new File(dir, element);
                if (!deleteDir(file)) {
                    sender.sendMessage(prefix+del_fail.replace("[file]", file.getAbsolutePath()));
                } else {
                    sender.sendMessage(prefix+del_complete.replace("[file]", file.getAbsolutePath()));
                }
            }
        }
        return dir.delete();
    }

    public boolean deleteDir(File dir) {
        return deleteDir((CommandSender) Bukkit.getConsoleSender(), dir);
    }
}
