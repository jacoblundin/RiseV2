package Model.player;

import Model.Tiles.TileCollection;

public class PlayerAndTiles {
    public TileCollection tileCollection;
    public PlayerList playerList;

    public PlayerAndTiles(TileCollection tileCollection, PlayerList playerList) {
        this.tileCollection = tileCollection;
        this.playerList = playerList;
    }
}
