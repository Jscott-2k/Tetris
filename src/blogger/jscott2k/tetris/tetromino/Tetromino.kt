package blogger.jscott2k.tetris.tetromino

import blogger.jscott2k.tetris.enums.Direction
import blogger.jscott2k.tetris.enums.ShiftStatus
import blogger.jscott2k.tetris.game.GameGrid
import blogger.jscott2k.tetris.utils.Vec2Int

class Tetromino(private val grid: GameGrid){

    private val scheme: TetrominoScheme = TetrominoScheme.getRandom()
    private var rotationIndex:Int = 0
    private var isGrounded:Boolean = false

    private val tiles: MutableList<TetrominoTile> = MutableList(size = 4) {
        val tile = TetrominoTile(parent = this, grid = grid)
        setTilePointFromScheme(it,tile)
        tile
    }

    init{
        println("Created new Tetromino")
        println("\tScheme: ${scheme.name}")
        print("\tPoints:")
        tiles.forEach {print(" ${it.getPoint()}")}
        println()

    }

    fun rotate(a:Int){
        rotationIndex - (rotationIndex + a) % 4
        tiles.forEach{
            it.rotate(rotationIndex)
        }
    }

    private fun setTilePointFromScheme(index:Int, tile: TetrominoTile) {
        tile.setIsPivot(scheme.getGridPoint(index) == scheme.getPivotPoint())
        val spawnPoint: Vec2Int = (scheme.getGridPoint(index) + grid.getSpawnPoint()) - scheme.getPivotPoint()
        tile.setPoint(point = spawnPoint)

    }
    override fun toString(): String {
        return scheme.getCharIdentifier().toString()
    }
    fun getGridPointsOfPieces(): Array<Vec2Int>{
        return Array(tiles.size){tiles[it].getPoint()}
    }
    fun getCharIdentifier(): Char {
        return scheme.getCharIdentifier()
    }

    fun setIsGrounded(isGrounded:Boolean){
        this.isGrounded = isGrounded
    }

    fun getIsGrounded():Boolean{
        return this.isGrounded
    }

    fun update(){
        tiles.forEach {
            if(it.getPoint().x == grid.getRows()-1){
                setIsGrounded(true)
            }
        }
    }

//    private fun getLeftMostPieces(): ArrayList<TetrominoPiece> {
//
//    }
//
//    private fun getRightMostPieces(): ArrayList<TetrominoPiece> {
//
//    }
//
//    private fun getBottomMostPieces(): ArrayList<TetrominoPiece> {
//
//    }

//    fun shiftWithNoCollision(shiftPoint: GridPoint) {
//        pieces.forEach {
//            it.shift(shiftPoint)
//        }
//    }

    fun shift(direction: Direction):ArrayList<ShiftStatus>{
        println("$this: SHIFTING DIRECTION: $direction")

        val shiftStatus:ArrayList<ShiftStatus> = ArrayList()
        tiles.forEach {
            shiftStatus.add(it.shift(direction))
        }
        tiles.forEach {
            var canUpdatePosition = true
            for (status: ShiftStatus in shiftStatus){
                if(status != ShiftStatus.SUCCESS){
                    canUpdatePosition = false
                    break
                }
            }
            if(canUpdatePosition){
                it.updateToPotentialPoint()
            }
        }
        println("\tSTATUS: $shiftStatus")
        return shiftStatus
    }

    fun findPieceAt(point: Vec2Int): TetrominoTile?{
        tiles.forEach {
            if(it.getPoint() == point){
                return it
            }
        }
        return null
    }

    fun getPivotTile(): TetrominoTile {
        tiles.forEach {
            if(it.getIsPivot()){
                return it
            }
        }
        return tiles[0]
    }

    fun removeTileAt(point: Vec2Int) {

        val tile:TetrominoTile? = tiles.find {
            it.getPoint() == point
        }

        if (tile != null) {
            tiles.remove(tile)
        }

    }
}