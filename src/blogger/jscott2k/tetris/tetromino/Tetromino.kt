package blogger.jscott2k.tetris.tetromino

import blogger.jscott2k.tetris.enums.Direction
import blogger.jscott2k.tetris.tetromino.TetrominoScheme
import blogger.jscott2k.tetris.enums.TileStatus
import blogger.jscott2k.tetris.game.GameGrid
import blogger.jscott2k.tetris.game.GameManager
import blogger.jscott2k.tetris.utils.RotationMatrix
import blogger.jscott2k.tetris.utils.Vec2Int


class Tetromino(private val grid: GameGrid){

    private val scheme: TetrominoScheme = TetrominoScheme.getRandom()
    private var rotationIndex:Int = 0
    private var isGrounded:Boolean = false
    private var canSpawn:Boolean = true
    private val tiles: MutableList<TetrominoTile> = MutableList(size = 4){initTile(it)}
    private var isPreservedForm:Boolean = true
    private var lockedInPlace:Boolean = false


    private fun initTile(index:Int):TetrominoTile{
        val tile = TetrominoTile(parent = this, grid = grid)
        if(!canSpawn){
            return tile
        }

        canSpawn = setTilePointFromScheme(index, tile)
        return tile
    }

    fun getTiles():MutableList<TetrominoTile>{
        return tiles
    }

    fun getTilesAtOrAboveRow(row:Int):MutableList<TetrominoTile>{

        val atiles:MutableList<TetrominoTile> = mutableListOf()

        tiles.forEach {
            if(it.getPoint().x <= row){
                atiles.add(it)
            }
        }
        return atiles
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

    fun rotate(a:Int):MutableMap<TetrominoTile, TileStatus>{

        if(lockedInPlace){return mutableMapOf(GameManager.getDefaultTile() to TileStatus.LOCKED)}

        val rotation:RotationMatrix =
            if(a<0){
                RotationMatrix.COUNTER_CLOCKWISE
            }else{
                RotationMatrix.CLOCKWISE
            }

        val tileStatuses:MutableMap<TetrominoTile, TileStatus> = mutableMapOf()

        tiles.forEach{
            tileStatuses[it] = it.rotate(rotation)
        }

        val canUpdate:Boolean = tileStatuses.values.none { it != TileStatus.SUCCESS }

        if(canUpdate){
            tileStatuses.keys.forEach{
                it.updateToPotentialPoint()
            }
        }

        return tileStatuses
    }

    private fun setTilePointFromScheme(index:Int, tile: TetrominoTile):Boolean {
        tile.setIsPivot(scheme.getGridPoint(index) == scheme.getPivotPoint())
        val spawnPoint: Vec2Int = ((scheme.getGridPoint(index)) + grid.getSpawnPoint()) - scheme.getPivotPoint()
        tile.setPoint(point = spawnPoint)
        return validSpawnPoint(point = spawnPoint)
    }

    private fun validSpawnPoint(point: Vec2Int): Boolean {
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
        return if(isPreservedForm)
             this.isGrounded
        else{
            //Finds a tile that is NOT grounded. If no tile was found then return true, otherwise return false
            tiles.find { !it.getIsGrounded() } == null
        }
    }

    fun update(){
        tiles.forEach {
            //Set this tetromino as grounded if any tile is at last row spot
            if(it.getPoint().x == grid.getRows()-1){
                this.setIsGrounded(true)
            }
        }
        shift(grid.getGravityDirection()) //Apply gravity
    }

    fun getIsPreservedForm():Boolean{
        return isPreservedForm
    }

    fun shift(direction: Direction, specificTile:TetrominoTile):TileStatus{

        if(lockedInPlace){ return TileStatus.LOCKED }
        if(isPreservedForm){return TileStatus.FAILED_WRONG_PRESERVATION}

        println("\t$this: SHIFTING DIRECTION: $direction")

        val tileToShift: TetrominoTile = specificTile.takeIf { it in this.tiles } ?: return TileStatus.FAILED_NULL_TILE
        val tileStatus:TileStatus = tileToShift.shift(direction)

        if(tileStatus == TileStatus.SUCCESS){
            tileToShift.updateToPotentialPoint()
        }
        println("\tSTATUS: $tileStatus")
        return tileStatus
    }

    fun shift(direction: Direction):MutableMap<TetrominoTile, TileStatus>{

        if(lockedInPlace){return mutableMapOf(GameManager.getDefaultTile() to TileStatus.LOCKED) }
        if(!isPreservedForm){return mutableMapOf(GameManager.getDefaultTile()  to TileStatus.FAILED_WRONG_PRESERVATION)}

        val tileStatuses:MutableMap<TetrominoTile, TileStatus> = mutableMapOf()

        println("\t$this: SHIFTING DIRECTION: $direction")

        tiles.forEach {
            tileStatuses[it] = it.shift(direction)
        }

        val canUpdatePosition:Boolean = tileStatuses.values.none { it != TileStatus.SUCCESS }

        if(canUpdatePosition){
            tileStatuses.keys.forEach {
                it.updateToPotentialPoint()
            }
        }
        println("\tSTATUS: $tileStatuses")
        return tileStatuses
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

    fun getTilesLeft():Int{
        return tiles.size
    }

    fun removeTileAt(point: Vec2Int):TetrominoTile? {
        val tile: TetrominoTile = tiles.find {it.getPoint() == point} ?: return null
        tiles.remove(tile)
        return tile
    }

    fun setIsPreservedForm(isPreservedForm: Boolean) {
        this.isPreservedForm = isPreservedForm
    }

    fun setLockedInPlace(lockedInPlace:Boolean) {
        this.lockedInPlace = lockedInPlace
    }

    fun shiftAllNotPreserved(direction: Direction) {
        tiles.forEach {
            this.shift(direction = direction, specificTile = it)
        }
    }
}