package blogger.jscott2k.tetris.utils

import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin

data class RotationMatrix(private val matrix:Array<Array<Double>>){

    companion object{

        private const val CLOCKWISE_MULTIPLIER = 1
        private const val COUNTER_CLOCKWISE_MULTIPLIER = -1

//        val CLOCKWISE:RotationMatrix = RotationMatrix(
//            arrayOf(
//                arrayOf(0.00,1.00),
//                arrayOf(-1.00, 0.00)
//            ))
//        val COUNTER_CLOCKWISE:RotationMatrix = RotationMatrix(
//            arrayOf(
//                arrayOf(0.00,-1.00),
//                arrayOf(1.00, 0.00))
//        )

        fun createRotationMatrix(clockwise:Boolean, theta:Double):RotationMatrix{

            val directionMod:Int = if(clockwise) CLOCKWISE_MULTIPLIER else COUNTER_CLOCKWISE_MULTIPLIER

            println("CREATING CLOCKWISE=$clockwise ROTATION MATRIX")

            return RotationMatrix(arrayOf(
                arrayOf(round(x = cos(theta)),-round(x= directionMod*sin(theta))),
                arrayOf(round(x = directionMod*sin(theta)), round(x = cos(theta)))))
        }

    }

    operator fun times(other:Vec2Int):Vec2Int{

        val rX:Int =  ((other.x * matrix[0][0]) + (matrix[0][1] * other.y)).toInt()
        val rY:Int = ((matrix[1][0] * other.x) + (matrix[1][1] * other.y)).toInt()

        return Vec2Int(x = rX, y = rY)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RotationMatrix

        if (!matrix.contentDeepEquals(other.matrix)) return false

        return true
    }

    override fun hashCode(): Int {
        return matrix.contentDeepHashCode()
    }

    override fun toString(): String {
        return "[[${matrix[0][0]},${matrix[0][1]}]," +
                "[${matrix[1][0]},${matrix[1][1]}]]"
    }
}