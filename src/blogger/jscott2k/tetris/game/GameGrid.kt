package blogger.jscott2k.tetris.game

import blogger.jscott2k.tetris.enums.Direction
import blogger.jscott2k.tetris.tetromino.Tetromino
import blogger.jscott2k.tetris.tetromino.TetrominoTile
import blogger.jscott2k.tetris.utils.Vec2Int


class GameGrid(private val rows: Int, private val cols: Int) {

    private val rowDeathRange:IntRange = 0 until 3
    private val colDeathRange:IntRange = 0 until cols
    private val tetrominos: ArrayList<Tetromino> = ArrayList()
    private val display: Array<Array<Char>> = Array(rows){Array(cols){' '}}
    private val colDisplayRange: IntRange = 0 until cols
    private val rowDisplayRange: IntRange = 2 until rows //First two rows hidden
    private val spawnPoint: Vec2Int = Vec2Int(x = 1, y = 4)
    private val gravityDirection: Direction = Direction.DOWN

    fun getGravityDirection():Direction{
        return gravityDirection
    }
    fun getRowDeathRange():IntRange{
        return this.rowDeathRange
    }
    fun getColDeathRange():IntRange{
        return this.colDeathRange
    }
    fun addTetromino(tetromino: Tetromino):Boolean {
        if(tetromino.getCanSpawn()){
            tetrominos.add(tetromino)
            return true
        }
        return false
    }

//    fun applyGravity(){
//        tetrominos.forEach {
//           it.shift(gravityDirection)
//        }
//    }

    fun requestPlayerSpawn():Boolean{
        return GameManager.createPlayerTetromino()
    }

    fun update(){

        //Update for each tetromino before display., i.e, test grounded, apply gravity...
        tetrominos.forEach{
            it.update()
        }

        //Remove filled rows
        removeFilledRows()

        //attempt to spawn player tetromino. Must be before update!
        requestPlayerSpawn()

        //Updates display
        updateDisplay()
    }

    private fun updateDisplay(){
        //Update display for each tetromino
        tetrominos.forEach {
            updateDisplayMapForTetromino(it)
        }
    }

    private fun updateDisplayMapForTetromino(tetromino: Tetromino){
        val vec2Ints:Array<Vec2Int> = tetromino.getGridPointsOfTiles()
        val charIdentifier:Char = tetromino.getCharIdentifier()
        vec2Ints.forEach {
            display[it.x][it.y] = charIdentifier
        }
    }

    fun getCharAtPoint(row:Int, col:Int): Char? {
        return display[row][col]
    }
    fun getCharAtPoint(point: Vec2Int): Char? {
        return display[point.x][point.y]
    }
    fun getTileAtPoint(point: Vec2Int): TetrominoTile?{
        tetrominos.forEach {
            val currentTetromino: Tetromino = it
            val tile: TetrominoTile? = currentTetromino.findTileAt(point)
            if(tile!=null){
                return tile
            }
        }
        return null
    }

    fun getTileAtPoint(row:Int, col:Int): TetrominoTile?{
        val point = Vec2Int(row, col)
        return getTileAtPoint(point)
    }

    fun display() {
        println()
        for(i:Int in 0..(rows.toString().length + (cols*2) + 4)){
            print("=")
        }
        println()
        for (row: Int in rowDisplayRange) {
            print("$row:")

            val spaces:Int = rows.toString().length - row.toString().length
            for(i:Int in 0..spaces){
                print(" ")
            }
            print("# ")

            for (col: Int in colDisplayRange) {
                print("${display[row][col]} ")
            }
            print("#")
            println()
        }
        for(i:Int in 0..(rows.toString().length + (cols*2) + 4)){
            print("=")
        }
        println()
    }
    fun clear() {
        for (row: Int in rowDisplayRange) {
            for (col: Int in colDisplayRange) {
                display[row][col] = ' '
            }
        }
    }

    fun shove() {
        tetrominos.forEach {
            while(!it.getIsGrounded()){
                it.shift(Direction.DOWN)
            }
        }
    }

    fun getCols(): Int {
        return cols
    }
    fun getRows():Int{
        return rows
    }
    private fun isRowFilled(row:Int):Boolean{
        for(col:Int in colDisplayRange){
            if(getTileAtPoint(row = row, col = col) != null){
                continue
            }
            return false
        }
        return true
    }
    private fun removeFilledRow(row:Int){
        println()
        println("REMOVING ROW: $row")
        for(col:Int in colDisplayRange){
            val point = Vec2Int(row, col)

            //Remove all tiles in that col
            tetrominos.forEach {
                it.removeTileAt(point).also{tile ->
                 if(tile!=null){
                     println("\tTILE $tile REMOVED!")
                 }
                }
            }
            println("\tTILES REMOVED!")

            //Remove tetrominos with 0 tiles left
            tetrominos.removeIf{
                val shouldRemove = it.getTilesLeft() == 0
                if(shouldRemove){
                    println("REMOVING TETROMINO $it: 0 TILES LEFT!")
                }
                shouldRemove
            }

            //Attempt to shift all tiles ABOVE the removed row DOWN
            tetrominos.forEach {
                val tilesAtOrAboveRow = it.getTilesAtOrAboveRow(row)
                if(tilesAtOrAboveRow.size>0){
                    println("\tSHIFTING TILES IN TETROMINO $it ABOVE ROW $row DOWN")
                    it.setIsGrounded(false)
                    while(!it.getIsGrounded()){
                        it.shift(gravityDirection)
                    }
                }
            }
        }
    }
    private fun removeFilledRows(){
        for(row:Int in rowDisplayRange){
            if(isRowFilled(row)){
                removeFilledRow(row)
            }
        }
    }

    fun getSpawnPoint(): Vec2Int {
        return spawnPoint
    }

    fun getHasTetrominos(): Boolean {
        return (tetrominos.size != 0)
    }
}