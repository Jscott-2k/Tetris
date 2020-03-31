package blogger.jscott2k.tetris.game

import blogger.jscott2k.tetris.tetromino.Tetromino
import blogger.jscott2k.tetris.tetromino.TetrominoScheme

/**
 * This class will manage all aspects the tetris game.
 * It will hold data for all tetrominos and manage player data
 *
 * @Author Justin Scott
 *
 * 3-28-20
 */
object GameManager {
    /**
     * Create the tetris grid 10 x 16
     */
    private val ROWS: Int = 16
    private val COLS: Int = 10
    private var player: Player =
        Player()


    private var grid: GameGrid =
        GameGrid(
            ROWS,
            COLS
        )
    private var running: Boolean = false

    fun start() {
        println("Starting...")


        //Initialize input actions
        InputManager.addInputAction(key = "end") {
            end()
        }
        InputManager.addInputAction(key = "rotate") {
            args ->  player.rotate(directionString = args[0])
        }
        InputManager.addInputAction(key = "move") { args ->
            player.move(directionString = args[0])
        }
        InputManager.addInputAction(key = "shove") {
            grid.shove()
        }
        InputManager.addInputAction(key = "only") {
            args ->
                val onlySchemes:MutableList<TetrominoScheme> = mutableListOf()
                args.forEach {
                    onlySchemes.add(TetrominoScheme.valueOf(it))
                }
                TetrominoScheme.setAllowedSchemes(onlySchemes)
        }

        //Clear grid
        grid.clear()

        //do game loop
        loop()
    }


    fun loop() {
        running = true
        sendPlayerTetromino()
        while (running) {
            nextFrame()
        }
    }

    private fun isInDeathRange():Boolean{
        for(row:Int in grid.getRowDeathRange()){
            for(col:Int in grid.getColDeathRange()){
                if(grid.getPieceAtPoint(row, col) != null){
                    return true
                }
            }
        }
        return false
    }

    fun sendPlayerTetromino():Boolean{


        if(isInDeathRange()){
            println("Game Over!")
            end()
            return false
        }

        val tetromino = Tetromino(grid)
        player.setTetromino(tetromino)
        grid.addTetromino(tetromino)
        return true
    }

    fun input() {
        print(">> ")
        val input: String? = readLine()
        if (input != null) {
            InputManager.invoke(input)
        }
    }

    /**
     *
     */
    fun end() {
        println("Stopping...")
        running = false
    }

    /**
     *
     */
    fun nextFrame() {
        grid.clear()

        if(player.getIsGrounded()){
            if(!sendPlayerTetromino()){
                return
            }
        }
        grid.update()
        grid.display()
        input()
    }
    fun getGrid(): GameGrid {
        return grid
    }
}