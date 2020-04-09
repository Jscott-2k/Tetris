package blogger.jscott2k.tetris

import blogger.jscott2k.tetris.game.Game
import java.lang.Exception
import java.lang.NumberFormatException

fun main(args:Array<String>) {
    var startLevel:Int = 1
    try {
        startLevel = args.getOrElse(0){"1"}.toInt()
    }catch (e:Exception){
        e.printStackTrace()
    }finally {
        Game.start(startLevel)
    }
}