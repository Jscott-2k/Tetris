package blogger.jscott2k.tetris.utils

data class Vec2Int(val x: Int, val y:Int){
    override operator fun equals(other:Any?):Boolean {
        return if(other is Vec2Int){
            (x == other.x && y == other.y)
        }else{
            false
        }
    }
    operator fun plus(other: Vec2Int): Vec2Int {
        return Vec2Int(x = x + other.x, y = y + other.y)
    }
    operator fun minus(other: Vec2Int): Vec2Int {
        return Vec2Int(x = x - other.x, y = y - other.y)
    }
    operator fun times(other: Vec2Int): Vec2Int {
        return Vec2Int(x = x * other.x, y = y * other.y)
    }
    override fun hashCode(): Int {
        var result:Int = x
        result = 31 * result + y
        return result
    }
}