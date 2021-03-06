package blogger.jscott2k.tetris.game.grid

import blogger.jscott2k.tetris.enums.Direction
import blogger.jscott2k.tetris.game.Game
import blogger.jscott2k.tetris.game.ImmovableTile
import blogger.jscott2k.tetris.game.Tile
import blogger.jscott2k.tetris.tetromino.Tetromino
import blogger.jscott2k.tetris.tetromino.TetrominoTile
import blogger.jscott2k.tetris.utils.Vec2Int
import kotlin.math.floor


class Grid(private var gridLoader: GridLoader, private val rows: Int, private val cols: Int) {

    constructor(gridLoader: GridLoader) : this(gridLoader, gridLoader.getRows(), gridLoader.getCols())

    init {
        println("\tGRID CONSTRUCTED FROM GRID LOADER: $gridLoader")
    }

    private val rowDisplayStart: Int = 2
    private val rowDeathRange: IntRange = 0 until 3
    private val colDeathRange: IntRange = 0 until cols
    private val tetrominoes: ArrayList<Tetromino> = ArrayList()
    private val display: Array<Array<Char>> = Array(rows) { Array(cols) { ' ' } }
    private val colDisplayRange: IntRange = 0 until cols
    private val rowDisplayRange: IntRange = rowDisplayStart until rows //First two rows hidden
    private val spawnPoint: Vec2Int = Vec2Int(x = 1, y = 4)
    private val gravityDirection: Direction = Direction.DOWN
    private var gravityEnabled: Boolean = true
    private var immovableTiles: MutableList<ImmovableTile> = ImmovableTile.genImmovableTiles(gridLoader)

    fun toggleGravity() {
        gravityEnabled = !gravityEnabled
        println("TOGGLED GRAVITY: gravityEnabled=$gravityEnabled")
    }

    fun getGravityEnabled(): Boolean {
        return gravityEnabled;
    }

    fun getGravityDirection(): Direction {
        return gravityDirection
    }

    fun getRowDeathRange(): IntRange {
        return this.rowDeathRange
    }

    fun getColDeathRange(): IntRange {
        return this.colDeathRange
    }

    fun addTetromino(tetromino: Tetromino): Boolean {
        if (tetromino.getCanSpawn()) {
            tetrominoes.add(tetromino)
            return true
        }
        return false
    }


    private fun requestPlayerSpawn(): Boolean {
        return Game.createPlayerTetromino()
    }

    fun update() {

        //Update for each tetromino before display., i.e, test grounded, apply gravity...
        tetrominoes.forEach {
            it.update()
        }

        //attempt to spawn player tetromino. Must be before update!
        if (requestPlayerSpawn()) {

            //Remove filled rows when next player spawned
            removeFilledRows()
        }

    }

    private fun updateDisplay() {

        //Update display for each tetromino
        tetrominoes.forEach {
            updateDisplayMapForTetromino(it)
        }
        println("UPDATE DISPLAYED IMMOVABLE")
        //Update display for immovables
        immovableTiles.forEach {
            val point: Vec2Int = it.getPoint()
            println("\t$it")
            display[point.x][point.y] = ImmovableTile.Identifier
        }

    }

    private fun updateDisplayMapForTetromino(tetromino: Tetromino) {
        val vec2Ints: Array<Vec2Int> = tetromino.getGridPointsOfTiles()
        val charIdentifier: Char = tetromino.getCharIdentifier()
        vec2Ints.forEach {
            display[it.x][it.y] = charIdentifier
        }
    }

    fun getCharAtPoint(row: Int, col: Int): Char? {
        return display[row][col]
    }

    fun getCharAtPoint(point: Vec2Int): Char? {
        return display[point.x][point.y]
    }

    fun getTileAtPoint(point: Vec2Int): Tile? {

        tetrominoes.forEach {
            val currentTetromino: Tetromino = it
            val tile: TetrominoTile? = currentTetromino.findTileAt(point)
            if (tile != null) {
                return tile
            }
        }

        return immovableTiles.find { immovableTile -> immovableTile.getPoint() == point }
    }

    fun getTileAtPoint(row: Int, col: Int): Tile? {
        val point = Vec2Int(row, col)
        return getTileAtPoint(point)
    }

    private fun printDisplay() {

        val gameDetailsPos: Int = floor(rows.toFloat() / 2f).toInt()

        println()
        for (i: Int in 0..(rows.toString().length + (cols * 2) + 4)) {
            print("=")
        }
        println()
        for (row: Int in rowDisplayRange) {

            print("$row:")

            val spaces: Int = rows.toString().length - row.toString().length
            for (i: Int in 0..spaces) {
                print(" ")
            }
            print("# ")

            for (col: Int in colDisplayRange) {
                print("${display[row][col]} ")
            }
            print("#")

            if (row == gameDetailsPos) {
                printGameDetailLine()
            }

            println()
        }
        for (i: Int in 0..(rows.toString().length + (cols * 2) + 4)) {
            print("=")
        }
        println()
    }

    private fun printGameDetailLine(offset: Int = 4) {
        val spaceOffset = String(Array(size = offset) { ' ' }.toCharArray())
        print(
            "$spaceOffset Next Tetromino:  ${Game.getNextPlayerScheme()};"
                    + " Score: ${Game.getScore()}; Lines Cleared: ${Game.getLinesCleared()};"
                    + " Level: ${Game.getLevel()}"
        )
    }

    fun display() {

        //Updates display
        updateDisplay()

        //Print display out
        printDisplay()
    }

    fun clear() {
        for (row: Int in rowDisplayRange) {
            for (col: Int in colDisplayRange) {
                display[row][col] = ' '
            }
        }
    }

    fun shove() {
        tetrominoes.forEach {
            while (!it.getIsGrounded()) {
                if (it.getIsPreservedForm()) {
                    it.shift(Direction.DOWN)
                } else {
                    it.shiftAllNotPreserved(Direction.DOWN)
                }
            }
        }
    }

    fun getCols(): Int {
        return cols
    }

    fun getRows(): Int {
        return rows
    }

    private fun isRowFilled(row: Int): Boolean {
        for (col: Int in colDisplayRange) {
            if (getTileAtPoint(row = row, col = col) != null) {
                continue
            }
            return false
        }
        return true
    }

    private fun removeEmptyTetrominoes() {
        tetrominoes.removeIf {
            val shouldRemove = (it.getTilesLeft() == 0)
            if (shouldRemove) {
                println("REMOVING TETROMINO $it: 0 TILES LEFT!")
            }
            shouldRemove
        }
    }

    private fun readyTileRowRealignment(rowRemoved: Int): MutableList<TetrominoTile> {
        println("\tFINDING TILES ABOVE ROW $rowRemoved TO BE REALIGNED...")

        val tilesAtOrAboveRow: MutableList<TetrominoTile> = mutableListOf()

        tetrominoes.forEach {
            //Retrieve all tiles above the removed row
            val additionalTilesAtOrAboveRow: MutableList<TetrominoTile> = it.getTilesAtOrAboveRow(rowRemoved)
            tilesAtOrAboveRow.addAll(additionalTilesAtOrAboveRow)

            //Only can be shiftable when at least one of the the tile are above the row
            if (additionalTilesAtOrAboveRow.size > 0) {
                println("\t\tFOUND: $additionalTilesAtOrAboveRow")
                it.setIsPreservedForm(false) //Have to shift tile by tile to avoid tetrominoes colliding when shifted down
                it.setIsGrounded(false)
            }
        }
        return tilesAtOrAboveRow
    }

    private fun alignTilesAfterRowRemoval(rowRemoved: Int) {

        val tilesAtOrAboveRow: MutableList<TetrominoTile> = readyTileRowRealignment(rowRemoved)

        println("\tSHIFTING TILES ABOVE ROW $rowRemoved DOWN")
        tilesAtOrAboveRow  //Tiles have to be first be sorted so that tiles further below get shifted first
            .sortedWith(compareByDescending { it.getPoint().x })
            .forEach {
                //Each tile needs to try and shift downwards. If it is unpreserved the whole parent will be shifted downwards
                println("\tTILE REALIGNED FROM: $it -> ${it.getPoint()}, ${it.parent.getIsPreservedForm()}")
                it.parent.shift(Direction.DOWN, it) //To shift a specific tile of an unpreserved tetromino
                println("\t\tTO: $it -> ${it.getPoint()}, ${it.parent.getIsPreservedForm()}")
            }
        tilesAtOrAboveRow.forEach {
            it.parent.setIsPreservedForm((it.parent.getTiles().size == 4))
        }
    }

    private fun removeTilesFromRow(row: Int) {
        println("\nREMOVING ROW: $row")
        for (col: Int in colDisplayRange) {
            val point = Vec2Int(row, col)

            //Remove all tiles in that col
            tetrominoes.forEach {
                it.removeTileAt(point).also { tile ->
                    if (tile != null) {
                        println("\tTILE -> $tile REMOVED!")
                    }
                }
            }
            println("REMOVING IMMOVABLE TILES")
            immovableTiles.removeIf {
                it.getPoint() == point
            }
        }
    }

    private fun removeFilledRows() {

        val removedRows: ArrayList<Int> = arrayListOf()

        for (row: Int in rowDisplayRange) {
            if (isRowFilled(row)) {

                //Remove tiles in the row
                removeTilesFromRow(row)

                //Remove tetrominoes with 0 tiles left
                removeEmptyTetrominoes()

                //Track rows removed
                removedRows.add(row)
            }
        }

        //Attempt to shift all tiles ABOVE the removed row DOWN
        removedRows.forEach { alignTilesAfterRowRemoval(it) }

        //Update score
        Game.addScore(removedRows.size)
    }

    fun getSpawnPoint(): Vec2Int {
        return spawnPoint
    }

    fun getHasTetrominos(): Boolean {
        return (tetrominoes.size != 0)
    }

    fun setLevel(level: Int) {

        gridLoader.loadLevel(level)
        immovableTiles = ImmovableTile.genImmovableTiles(gridLoader)

    }

    fun dispose() {
        println("DISPOSING GRID!")
        tetrominoes.clear()
        immovableTiles.clear()
    }
}