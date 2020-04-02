package blogger.jscott2k.tetris.utils

data class RotationMatrix(val matrix:Array<Array<Int>>){

    companion object{
        val CLOCKWISE:RotationMatrix = RotationMatrix(
            arrayOf(
                arrayOf(0,1),
                arrayOf(-1, 0)
            ))
        val COUNTER_CLOCKWISE:RotationMatrix = RotationMatrix(
            arrayOf(
                arrayOf(0,-1),
                arrayOf(1, 0))
            )
    }

    operator fun times(other:Vec2Int):Vec2Int{
        return Vec2Int(
            x = (matrix[0][0] * other.x) + (matrix[0][1] * other.y),
            y = (matrix[1][0] * other.x) + (matrix[1][1] * other.y))
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
}