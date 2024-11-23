package cn.ChengZhiYa.LangUtil.manager.server;

import org.bukkit.Bukkit;

import java.lang.reflect.Field;

public final class ServerManager {
    /**
     * 获取服务器版本
     *
     * @return 服务器当前版本例如:1.20.4
     */
    public String getServerVersion() {
        try {
            Field consoleField = Bukkit.getServer().getClass().getDeclaredField("console");
            consoleField.setAccessible(true);
            Object console = consoleField.get(Bukkit.getServer());
            return String.valueOf(console.getClass().getSuperclass().getMethod("getVersion").invoke(console));
        } catch (Exception e) {
            return Bukkit.getServer().getBukkitVersion().split("-")[0];
        }
    }

    public String getNMSVersion() {
        switch (getServerVersion()) {
            case "1.8":
            case "1.8.1":
            case "1.8.2":
                return "v1_8_R1";
            case "1.8.3":
                return "v1_8_R2";
            case "1.8.4":
            case "1.8.5":
            case "1.8.6":
            case "1.8.7":
            case "1.8.8":
                return "v1_8_R3";
            case "1.9":
            case "1.9.1":
            case "1.9.2":
            case "1.9.3":
                return "v1_9_R1";
            case "1.9.4":
                return "v1_9_R2";
            case "1.10":
            case "1.10.1":
            case "1.10.2":
                return "v1_10_R1";
            case "1.11":
            case "1.11.1":
            case "1.11.2":
                return "v1_11_R1";
            case "1.12":
            case "1.12.1":
            case "1.12.2":
                return "v1_12_R1";
            case "1.13":
            case "1.13.1":
                return "v1_13_R1";
            case "1.13.2":
                return "v1_13_R2";
            case "1.14":
            case "1.14.1":
            case "1.14.2":
            case "1.14.3":
            case "1.14.4":
                return "v1_14_R1";
            case "1.15":
            case "1.15.1":
            case "1.15.2":
                return "v1_15_R1";
            case "1.16":
            case "1.16.1":
                return "v1_16_R1";
            case "1.16.2":
            case "1.16.3":
                return "v1_16_R2";
            case "1.16.4":
            case "1.16.5":
                return "v1_16_R3";
            case "1.17":
            case "1.17.1":
                return "v1_17_R1";
            case "1.18":
            case "1.18.1":
                return "v1_18_R1";
            case "1.18.2":
                return "v1_18_R2";
            case "1.19":
            case "1.19.1":
            case "1.19.2":
                return "v1_19_R1";
            case "1.19.3":
                return "v1_19_R2";
            case "1.19.4":
                return "v1_19_R3";
            case "1.20":
            case "1.20.1":
                return "v1_20_R1";
            case "1.20.2":
                return "v1_20_R2";
            case "1.20.3":
            case "1.20.4":
                return "v1_20_R3";
            case "1.20.5":
            case "1.20.6":
                return "v1_20_R4";
            case "1.21":
                return "v1_21_R1";
            default:
                return null;
        }
    }
}
