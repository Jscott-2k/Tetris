package blogger.jscott2k.tetris.game

import blogger.jscott2k.tetris.game.grid.Grid
import blogger.jscott2k.tetris.game.grid.GridLoader
import blogger.jscott2k.tetris.tetromino.Tetromino
import blogger.jscott2k.tetris.tetromino.Scheme.TetrominoScheme
import blogger.jscott2k.tetris.tetromino.TetrominoTile
import jdk.nashorn.internal.objects.annotations.Setter

/**
 * This class will manage all aspects the tetris game.
 * It will hold data for all tetrominos and manage player data
 *
 * @Author Justin Scott
 *
 * 3-28-20
 */
object Game {

    /**
     * Create the tetris grid 10 x 22
     */
    private const val ROWS: Int = 22
    private const val COLS: Int = 10

    private var player: Player = Player()

    private var grid:Grid = Grid(GridLoader(rows = ROWS, cols = COLS))

    private var isRunning: Boolean = false
    private var allowAdditionalInput:Boolean = false

    private var linesCleared:Int = 0

    private var score:Int = 0
    private var level:Int = 1
    private var nextPlayerScheme:TetrominoScheme? = null



    fun getNextPlayerScheme():TetrominoScheme?{
        return nextPlayerScheme
    }
    fun setNextPlayerScheme(nextScheme:TetrominoScheme){
        this.nextPlayerScheme = nextScheme
    }

    fun getScore():Int{
        return score
    }
    fun addScore(rowsCleared:Int){

        if(rowsCleared<=0){return}

        score += when(rowsCleared){
            1 -> 40
            2 -> 100
            3 -> 300
            4 -> 1200
            else -> 1200
        }
        linesCleared += rowsCleared
    }

    fun getDefaultTile():TetrominoTile{
        return TetrominoTile(Tetromino.getPlaceHolderTetromino(), grid = grid)
    }
    private fun initializeInputActions(){
        InputManager.addInputAction(key = "end") {args ->
            end()
            return@addInputAction true
        }

        InputManager.addInputAction(key = "rotate") {args ->
            player.rotate(
                rotationDirection = args.getOrElse(index = 0){"RIGHT"},
                amount = args.getOrElse(index = 1){"1"})
            return@addInputAction false
        }

        InputManager.addInputAction(key = "move") { args ->

            player.move(
                directionString = (args.getOrElse(index = 0){"DOWN"}),
                amount = (args.getOrElse(index = 1){"1"})
            )
            return@addInputAction false
        }
        InputManager.addInputAction(key = "shove") {args ->
            grid.shove()
            return@addInputAction false
        }
        InputManager.addInputAction(key = "only") { args ->
            val onlySchemes:MutableList<TetrominoScheme> = mutableListOf()
            args.forEach {
                onlySchemes.add(TetrominoScheme.valueOf(it.toUpperCase()))
            }
            TetrominoScheme.setAllowedSchemes(onlySchemes)
            return@addInputAction true
        }
        InputManager.addInputAction(key = "next") {
            return@addInputAction false
        }
        InputManager.addInputAction(key = "gravity"){
            grid.toggleGravity()
            return@addInputAction true
        }
        InputManager.addInputAction(key = "level"){args->
            clearGameData()
            level = args.getOrElse(index = 0){"1"}.toInt()
            grid.setLevel(level)
            return@addInputAction false
        }
    }

    fun clearGameData(){
        this.score = 0
        this.linesCleared = 0
        grid.dispose()
    }

    fun start(level:Int = 1) {

        println("Starting...")

        Game.level = level
        grid = Grid(GridLoader(level = level, rows = ROWS, cols = COLS))
        //Initialize input actions
        initializeInputActions()

        //Clear grid
        grid.clear()

        //do game loop
        loop()
    }

    fun loop() {
        isRunning = true
        while (isRunning) {
            nextFrame()
        }
    }

    private fun isNonPlayerTetrominoTile(tile:TetrominoTile?):Boolean{
        return (tile?.parent != player.getTetromino()) && player.getTetromino() != null
    }

    private fun isInDeathRange():Boolean{
        for(row:Int in grid.getRowDeathRange()){
            for(col:Int in grid.getColDeathRange()){
                val tile:Tile? = grid.getTileAtPoint(row, col)

                if(tile != null && tile is TetrominoTile){ //Does tile exist and is it a tetromino tile?
                    if(isNonPlayerTetrominoTile(tile)){ //is it NOT a tile of the player tetromino?
                        println("NON-PLAYER TILE IN DEATH RANGE: $tile")
                        return true
                    }
                }
            }
        }
        return false
    }

    fun createPlayerTetromino():Boolean{

        /**
         *  If current player tetromino is NOT grounded, FAIL to create another player tetromino. Returns FALSE when player's tetromino null!
         *  Grounding is not required when there is 0 tetrominos on the grid, so getHasTetrominos() must be TRUE.
         */
        if(!player.getIsGrounded() && grid.getHasTetrominos()){
            return false
        }

        val tetromino = Tetromino(grid)
        val tetrominoAdded = grid.addTetromino(tetromino)

        player.setTetromino(tetromino) //Must be called BEFORE isInDeathRange()! Otherwise will register as game over!

        if(isInDeathRange() || !tetrominoAdded){
            end(msg = "Game Over!")
            return false
        }
        return true
    }

    private fun input(): Boolean {
        print(">> ")
        var input: String = readLine() ?: "next"
        if(input == ""){
            input = "next"
        }
        return InputManager.invoke(input )
    }

    /**
     * Method for ending the game. Sets isRunning to false
     * @param msg: message for output
     */
    private fun end(msg:String = "") {
        println(msg)
        println("Stopping...")
        isRunning = false
    }

    /**
     * The game is played frame by frame. At end of each frame, user has opportunity to send command.
     * Each frame has 4 processes. Clear, Update, Display, and User Input
     */
    private fun nextFrame() {
        if(!allowAdditionalInput){
            grid.clear()
            grid.update()
            grid.display()
        }
        allowAdditionalInput = input()
    }
    fun getGrid(): Grid {
        return grid
    }
    fun getPlayer():Player{
        return player
    }
    fun getLinesCleared():Int{
        return linesCleared
    }

    fun generateScheme(): TetrominoScheme {
        if(nextPlayerScheme==null){
            nextPlayerScheme = TetrominoScheme.getRandom()
        }
        val scheme: TetrominoScheme = nextPlayerScheme ?: TetrominoScheme.getRandom()
        nextPlayerScheme = TetrominoScheme.getRandom()
        return scheme
    }

    fun getLevel(): Int {
        return level
    }
}