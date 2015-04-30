package derekwilson.net.rameater.activity.settings;

public interface IPreferencesHelper {
    int getMaxMemoryMb();
    void setMaxMemoryMb(int max);
    void setMaxMemoryMb(String max);
    int getDefaultMaxMemoryMb();
}
