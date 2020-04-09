package blogger.jscott2k.tetris.game

import blogger.jscott2k.tetris.enums.TileStatus
import blogger.jscott2k.tetris.utils.Vec2Int

open class Tile(private var point:Vec2Int = Vec2Int(0,0)) {

    private var tileStatus: TileStatus = TileStatus.WAITING

    fun setPoint(point: Vec2Int){
        this.point = point
    }
    fun getPoint(): Vec2Int {
        return point
    }
    fun setStatus(tileStatus: TileStatus) {
        this.tileStatus = tileStatus
    }
    fun getStatus():TileStatus {
        return this.tileStatus
    }
}
