package blogger.jscott2k.tetris.tetromino

import blogger.jscott2k.tetris.enums.Direction
import blogger.jscott2k.tetris.game.GameGrid
import blogger.jscott2k.tetris.utils.Vec2Int
import blogger.jscott2k.tetris.enums.ShiftStatus

class TetrominoTile(val parent: Tetromino, val grid: GameGrid){

    private var point: Vec2Int =
        Vec2Int(x = 0, y = 0)
    private var potentialPoint: Vec2Int =
        Vec2Int(x = 0, y = 0)
    private var isPivot:Boolean = false

    fun setPoint(row:Int, col:Int){
        this.point = Vec2Int(row, col)
    }
    fun setPoint(point: Vec2Int){
        this.point = point
    }
    fun getPoint(): Vec2Int {
        return point
    }
//    fun shift(row: Int, col:Int){
//        val shiftPoint = GridPoint(row, col)
//        setPoint(this.point + shiftPoint)
//    }

    fun shift(direction: Direction, shiftPoint: Vec2Int): ShiftStatus {

        potentialPoint = (this.point + shiftPoint)
        val potentialCollidedTile: TetrominoTile? = grid.getPieceAtPoint(potentialPoint)

        if(parent.getIsGrounded()){
            return ShiftStatus.GROUNDED
        }
        else if(potentialPoint.y >= grid.getCols() ){
            return ShiftStatus.OUTSIDE_COLUMN_RIGHT
        }
        else if(potentialPoint.y < 0){
            return ShiftStatus.OUTSIDE_COLUMN_LEFT
        }
        else if(potentialPoint.x >= grid.getRows()){
            parent.setIsGrounded(true)
            return ShiftStatus.OUTSIDE_ROW_BOTTOM
        }
        else if(potentialPoint.x < 0){

            return ShiftStatus.OUTSIDE_ROW_TOP
        }
        else if(potentialCollidedTile!=null && potentialCollidedTile.parent != this.parent){
            if(direction == Direction.DOWN){
                parent.setIsGrounded(true)
            }
            return ShiftStatus.COLLISION_WITH_PIECE
        }
        else{
            return ShiftStatus.SUCCESS
        }
    }

    fun updateToPotentialPoint(){
        setPoint(potentialPoint)
        potentialPoint = Vec2Int(x = 0, y = 0)
    }

    fun shift(direction: Direction): ShiftStatus {
        val shiftPoint: Vec2Int = when(direction){
            Direction.DOWN -> Vec2Int(x = 1, y = 0)
            Direction.RIGHT -> Vec2Int(x = 0, y = 1)
            Direction.LEFT -> Vec2Int(x = 0, y = -1)
            else -> Vec2Int(x = 0, y = 0)
        }
        return shift(direction, shiftPoint)
    }

    fun rotate(rotationIndex: Int) {
        potentialPoint = Vec2Int(0,0) * parent.getPivotTile().getPoint()
    }

    fun getIsPivot(): Boolean {
        return this.isPivot
    }
    fun setIsPivot(isPivot:Boolean){
        this.isPivot = isPivot
    }
}