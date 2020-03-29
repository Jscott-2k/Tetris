package blogger.jscott2k.tetris.game

import blogger.jscott2k.tetris.enums.Direction
import blogger.jscott2k.tetris.enums.ShiftStatus
import blogger.jscott2k.tetris.tetromino.Tetromino
import java.lang.IllegalArgumentException

class Player{

    private var tetromino: Tetromino? = null
    fun move(directionString: String):Boolean{
        try{
            val direction: Direction = Direction.valueOf(directionString.toUpperCase())
            tetromino?.shift(direction)
            return true
        }catch(e:IllegalArgumentException){
            println("Invalid Argument {$directionString}!")
        }
        return false
    }
    fun setTetromino(tetromino: Tetromino){
        this.tetromino = tetromino
    }
    fun getTetromino(): Tetromino?{
        return this.tetromino
    }
    fun getIsGrounded():Boolean{
        return if(tetromino!=null){
            tetromino!!.getIsGrounded()
        } else{
            false
        }
    }

    fun rotate(directionString: String) {
        try{
            val directionStringFormatted:String = directionString.toUpperCase()
            val direction:Direction = Direction.valueOf(directionStringFormatted)

            if(direction == Direction.RIGHT)
                tetromino?.rotate(1)
            else{
                tetromino?.rotate(-1)
            }

        }catch(e:IllegalArgumentException){
            println("Invalid Argument! {$directionString}")
        }
    }
}