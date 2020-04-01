package blogger.jscott2k.tetris.tetromino

import blogger.jscott2k.tetris.enums.Direction
import blogger.jscott2k.tetris.game.GameGrid
import blogger.jscott2k.tetris.utils.Vec2Int
import blogger.jscott2k.tetris.enums.ShiftStatus

class TetrominoTile(val parent: Tetromino, val grid: GameGrid){

    private var point: Vec2Int = Vec2Int(x = 0, y = 0)
    private var potentialPoint: Vec2Int = Vec2Int(x = 0, y = 0)
    private var isPivot:Boolean = false
    private var isGrounded:Boolean = false
    private var shiftStatus:ShiftStatus = ShiftStatus.WAITING

    fun setIsGrounded(isGrounded:Boolean){
        this.isGrounded = isGrounded
    }
    fun getIsGrounded():Boolean{
        return this.isGrounded
    }
    fun setPoint(row:Int, col:Int){
        this.point = Vec2Int(row, col)
    }
    fun setPoint(point: Vec2Int){
        this.point = point
    }
    fun getPoint(): Vec2Int {
        return point
    }

    private fun shift(direction: Direction, shiftPoint: Vec2Int): ShiftStatus {

        potentialPoint = (this.point + shiftPoint)
        val potentialCollidedTile: TetrominoTile? = grid.getTileAtPoint(potentialPoint)

        if(parent.getIsGrounded() && parent.getIsPreservedForm()){
            return ShiftStatus.GROUNDED_WITH_FORM_PRESERVED
        }else if((!parent.getIsPreservedForm()) && (this.isGrounded)){
            return ShiftStatus.GROUNDED_WITH_FORM_NOT_PRESERVED
        }
        else if(potentialPoint.y >= grid.getCols() ){
            return ShiftStatus.OUTSIDE_COLUMN_RIGHT
        }
        else if(potentialPoint.y < 0){
            return ShiftStatus.OUTSIDE_COLUMN_LEFT
        }
        else if(potentialPoint.x >= grid.getRows()){
            this.onTileBottomRowCollision()
            return ShiftStatus.OUTSIDE_ROW_BOTTOM
        }
        else if(potentialPoint.x < 0){
            return ShiftStatus.OUTSIDE_ROW_TOP
        }
        else if(potentialCollidedTile!=null){
            return this.onTileTileCollision(direction, potentialCollidedTile)
        }
        else{
            return ShiftStatus.SUCCESS
        }
    }

    private fun onTileBottomRowCollision(){
        if(parent.getIsPreservedForm()){
            parent.setIsGrounded(true)
        }else{
            this.isGrounded = true
        }
    }
    private fun downTileTileCollision(otherTile:TetrominoTile):ShiftStatus{
        if(parent.getIsPreservedForm()){
            if(otherTile.parent == parent){
                return ShiftStatus.SUCCESS
            }
            parent.setIsGrounded(true)
        }else{
            this.isGrounded = true
        }

        return ShiftStatus.COLLISION_WITH_TILE
    }


    private fun tileTileCollision(otherTile: TetrominoTile):ShiftStatus{
        return if(otherTile.parent == parent){
             ShiftStatus.SUCCESS
        }else{
            ShiftStatus.COLLISION_WITH_TILE
        }

    }

    private fun onTileTileCollision(direction:Direction, otherTile:TetrominoTile):ShiftStatus{

        return when(direction){
            Direction.DOWN -> downTileTileCollision(otherTile)
            Direction.RIGHT -> tileTileCollision(otherTile)
            Direction.LEFT -> tileTileCollision(otherTile)
            else -> ShiftStatus.SUCCESS
        }
    }

    fun updateToPotentialPoint(){
        setPoint(potentialPoint)
        potentialPoint = Vec2Int(x = 0, y = 0)
    }

    fun prepareForShift(){
        this.shiftStatus = ShiftStatus.WAITING
    }
    fun isWaitingForShift():Boolean{
        return shiftStatus == ShiftStatus.WAITING
    }

    fun shift(direction: Direction): ShiftStatus {

        val shiftPoint: Vec2Int = when(direction){
            Direction.DOWN -> Vec2Int(x = 1, y = 0)
            Direction.RIGHT -> Vec2Int(x = 0, y = 1)
            Direction.LEFT -> Vec2Int(x = 0, y = -1)
            else -> Vec2Int(x = 0, y = 0)
        }
        shiftStatus = shift(direction, shiftPoint)
        return shiftStatus
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