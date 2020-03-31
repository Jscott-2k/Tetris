package blogger.jscott2k.tetris.tetromino

import blogger.jscott2k.tetris.enums.Direction
import blogger.jscott2k.tetris.enums.ShiftStatus
import blogger.jscott2k.tetris.game.GameGrid
import blogger.jscott2k.tetris.utils.Vec2Int


class Tetromino(private val grid: GameGrid){

    private val scheme: TetrominoScheme = TetrominoScheme.getRandom()
    private var rotationIndex:Int = 0
    private var isGrounded:Boolean = false
    private var canSpawn:Boolean = true
    private val tiles: MutableList<TetrominoTile> = MutableList(size = 4){initTile(it)}

    private fun initTile(index:Int):TetrominoTile{
        val tile = TetrominoTile(parent = this, grid = grid)
        if(!canSpawn){
            return tile
        }
        canSpawn = setTilePointFromScheme(index, tile)
        return tile
    }


    init{
        println("Created new Tetromino")
        println("\tScheme: ${scheme.name}")
        print("\tPoints:")
        tiles.forEach {print(" ${it.getPoint()}")}
        println()
        println("\tPivot:${scheme.getPivotPoint()}")
        println("\tCan Spawn:$canSpawn")

    }

    fun getCanSpawn():Boolean{
        return canSpawn
    }

    fun rotate(a:Int){
        rotationIndex - (rotationIndex + a) % 4
        tiles.forEach{
            it.rotate(rotationIndex)
        }
    }

    private fun setTilePointFromScheme(index:Int, tile: TetrominoTile):Boolean {
        tile.setIsPivot(scheme.getGridPoint(index) == scheme.getPivotPoint())
        val spawnPoint: Vec2Int = ((scheme.getGridPoint(index)) + grid.getSpawnPoint()) - scheme.getPivotPoint()
        tile.setPoint(point = spawnPoint)
        return validSpawnpoint(point = spawnPoint)
    }

    private fun validSpawnpoint(point: Vec2Int): Boolean {
        return (point.x >= 0 && point.y >= 0) && (point.x < grid.getRows() && point.y < grid.getCols())
    }

    override fun toString(): String {
        return scheme.getCharIdentifier().toString()
    }
    fun getGridPointsOfTiles(): Array<Vec2Int>{
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
            //Set this tetromino as grounded if any tile is at last row spot
            if(it.getPoint().x == grid.getRows()-1){
                setIsGrounded(true)
            }
        }
        shift(grid.getGravityDirection()) //Apply gravity
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

    fun findTileAt(point: Vec2Int): TetrominoTile?{
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