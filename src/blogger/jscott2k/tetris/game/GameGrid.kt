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
    private val spawnPoint: Vec2Int = Vec2Int(x = 0, y = 4)
    private val gravityDirection: Direction = Direction.DOWN

    fun getRowDeathRange():IntRange{
        return this.rowDeathRange
    }
    fun getColDeathRange():IntRange{
        return this.colDeathRange
    }
    fun addTetromino(tetromino: Tetromino) {
        tetrominos.add(tetromino)
    }
    fun applyGravity(){
        tetrominos.forEach {
            it.shift(gravityDirection)
        }
    }
    fun update(){

        //Update status of each tetromino., i.e, is it grounded?
        tetrominos.forEach{
            it.update()
        }

        //Apply gravity shifts
        applyGravity()

        //Remove filled rows
        removeFilledRow()

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
        val vec2Ints:Array<Vec2Int> = tetromino.getGridPointsOfPieces()
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
    fun getPieceAtPoint(point: Vec2Int): TetrominoTile?{
        tetrominos.forEach {
            val currentTetromino: Tetromino = it
            val tile: TetrominoTile? = currentTetromino.findPieceAt(point)
            if(tile!=null){
                return tile
            }
        }
        return null
    }

    fun getPieceAtPoint(row:Int, col:Int): TetrominoTile?{
        val point = Vec2Int(row, col)
        return getPieceAtPoint(point)
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
    fun isRowFilled(row:Int):Boolean{
        for(col:Int in colDisplayRange){
            if(getPieceAtPoint(row = row, col = col) != null){
                continue
            }
            return false
        }
        return true
    }

    fun removeFilledRow(){

            tetrominos.forEach {

                for(row:Int in rowDisplayRange){

                if(!isRowFilled(row)){
                    continue
                }

                for(col:Int in colDisplayRange){
                    val point = Vec2Int(row, col)
                    val tile:TetrominoTile? = it.findPieceAt(point)
                    if(tile!=null){
                        it.removeTileAt(point)
                    }
                }

               }
            }
    }

    fun getSpawnPoint(): Vec2Int {
        return spawnPoint
    }
}