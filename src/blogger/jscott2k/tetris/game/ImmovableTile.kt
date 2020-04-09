package blogger.jscott2k.tetris.game
import blogger.jscott2k.tetris.game.grid.GridLoader
import blogger.jscott2k.tetris.utils.Vec2Int
class ImmovableTile(point: Vec2Int = Vec2Int(x = 0,y = 0)): Tile(point) {

    companion object {
        const val Identifier: Char = 'X'
        fun genImmovableTiles(loader: GridLoader):MutableList<ImmovableTile>{
            val vecData:List<Vec2Int> = loader.getLevelData()
            return MutableList(vecData.size){
                val vec:Vec2Int = vecData[it]
                println("\tGENERATING IMMOVABLE TILE -> $vec")
                ImmovableTile(vec)
            }
        }
    }
}