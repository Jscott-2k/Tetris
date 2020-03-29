package blogger.jscott2k.tetris.tetromino

import blogger.jscott2k.tetris.utils.Vec2Int

data class TetrominoArrangement(val configure:Array<BooleanArray>) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TetrominoArrangement

        if (!configure.contentDeepEquals(other.configure)) return false

        return true
    }

    override fun hashCode(): Int {
        return configure.contentDeepHashCode()
    }

    fun getAsOriginGridPoints():ArrayList<Vec2Int>{
        val points:ArrayList<Vec2Int> = ArrayList()
        for(row:Int in configure.indices){
            for(col:Int in configure[0].indices){

                if(configure[row][col]){
                    points.add(Vec2Int(row, col))
                }
            }
        }
        return points
    }
}