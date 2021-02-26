package Model.player;

import java.util.List;

public class PlayerFromJSON {
    public String name;
    public int gold;
    public int netWorth;
    public String rank;
    public String color;
    public List<Integer> owns;

    @Override
    public String toString() {
        return "PlayerFromJSON{" +
                "name='" + name + '\'' +
                ", gold=" + gold +
                ", netWorth=" + netWorth +
                ", rank='" + rank + '\'' +
                ", color='" + color + '\'' +
                ", owns=" + owns +
                '}';
    }
}
