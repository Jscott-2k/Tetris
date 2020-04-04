package blogger.jscott2k.tetris.game

import blogger.jscott2k.tetris.enums.Direction
import blogger.jscott2k.tetris.tetromino.Tetromino
import kotlin.math.absoluteValue

class Player{

    private var tetromino: Tetromino? = null
    private val MAX_SHIFTS_PER_FRME:Int = 3

    fun move(directionString: String, amount: String):Boolean{

        val amountAsInt:Int = amount.toInt()
        amountAsInt.coerceIn(minimumValue = 1,maximumValue = MAX_SHIFTS_PER_FRME)
        val direction: Direction = Direction.valueOf(directionString.toUpperCase())

        for(i:Int in 1..amountAsInt){
            tetromino?.shift(direction)
        }

        return true
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

    fun rotate(rotationDirection: String, amount:String) {

        println("PLAYER:$tetromino: ROTATING")

        if(rotationDirection.toUpperCase() == "LEFT")
            println("\tSTATUS: ${tetromino?.rotate(-(amount.toInt().absoluteValue))}")

        else{
            println("\tSTATUS: ${tetromino?.rotate(amount.toInt().absoluteValue)}")
        }
    }
}