package krincraft.idk;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class IDKMessageConfig {
    protected IDK idk;
    private File file;
    protected FileConfiguration config;

    public IDKMessageConfig(IDK idk, String fileName) {
        this.idk = idk;
        this.file = new File(idk.getDataFolder(), fileName);
        if(!file.exists()) {
            try {
                file.createNewFile();
                FileWriter fw = new FileWriter(file);
                fw.write("reload: \"Config reloaded!\"\n" +
                        "failed: \"Config reload failed! Check details below!\"\n" +
                        "failed_p: \"Config reload failed! Check console for more information.\"\n" +
                        "survival: \"You're now in SURVIVAL!\"\n" +
                        "creative: \"You're now in CREATIVE!\"\n" +
                        "adventure: \"You're now in ADVENTURE!\"\n" +
                        "spectator: \"You're now in SPECTATOR!\"\n" +
                        "ping: \"Your ping is: \"\n" +
                        "no-console: \"Console not avalible right now.\"");
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public String getString(String s) {
        String a = this.config.getString(s);
        if (a != null) {
            return a;
        } else {
            return "null";
        }
    }

    public void reload(String fileName) {
        this.file = new File(idk.getDataFolder(), fileName);
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
