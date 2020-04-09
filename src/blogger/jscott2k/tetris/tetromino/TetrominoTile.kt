package blogger.jscott2k.tetris.tetromino

import blogger.jscott2k.tetris.enums.Direction
import blogger.jscott2k.tetris.enums.TileStatus
import blogger.jscott2k.tetris.game.Tile
import blogger.jscott2k.tetris.game.grid.Grid
import blogger.jscott2k.tetris.utils.RotationMatrix
import blogger.jscott2k.tetris.utils.Vec2Int

class TetrominoTile(val parent: Tetromino, val grid: Grid): Tile() {

    private var potentialPoint: Vec2Int = Vec2Int(x = 0, y = 0)
    private var isPivot:Boolean = false
    private var isGrounded:Boolean = false

    fun setIsGrounded(isGrounded:Boolean){
        this.isGrounded = isGrounded
    }
    fun getIsGrounded():Boolean{
        return this.isGrounded
    }

    private fun getTranslationStatus(direction: Direction):TileStatus {
        val potentialCollidedTile: Tile? = grid.getTileAtPoint(potentialPoint)
        return when {
            parent.getIsGrounded() && parent.getIsPreservedForm() -> TileStatus.GROUNDED_WITH_FORM_PRESERVED
            (!parent.getIsPreservedForm()) && (this.isGrounded) -> TileStatus.GROUNDED_WITH_FORM_NOT_PRESERVED

            potentialPoint.y >= grid.getCols() -> TileStatus.OUTSIDE_COLUMN_RIGHT
            potentialPoint.y < 0 -> TileStatus.OUTSIDE_COLUMN_LEFT

            potentialPoint.x >= grid.getRows() -> {
                this.onTileBottomRowCollision()
                TileStatus.OUTSIDE_ROW_BOTTOM
            }
            potentialPoint.x < 0 -> TileStatus.OUTSIDE_ROW_TOP

            potentialCollidedTile != null -> this.onTileTileCollision(direction, potentialCollidedTile)
            else -> TileStatus.SUCCESS
        }
    }
    private fun shift(direction: Direction, shiftPoint: Vec2Int): TileStatus {

        potentialPoint = (super.getPoint() + shiftPoint)
        return getTranslationStatus(direction)
    }

    private fun onTileBottomRowCollision(){
        if(parent.getIsPreservedForm()){
            parent.setIsGrounded(true)
        }else{
            this.isGrounded = true
        }
    }
    private fun downTileTileCollision(otherTile:TetrominoTile):TileStatus{
            if(parent.getIsPreservedForm()){
                if(otherTile.parent == parent){
                    return TileStatus.SUCCESS
                }
                parent.setIsGrounded(true)
            }else{
                this.isGrounded = true
            }
        return TileStatus.COLLISION_WITH_TILE
    }


    private fun tileTileCollision(otherTile: TetrominoTile):TileStatus{

        return if(otherTile.parent == parent){
             TileStatus.SUCCESS
        }else{
            TileStatus.COLLISION_WITH_TILE
        }

    }

    private fun onTileTileCollision(direction:Direction, otherTile:Tile):TileStatus{

        return if(otherTile is TetrominoTile){
            when(direction){
                Direction.DOWN -> downTileTileCollision(otherTile)
                Direction.RIGHT -> tileTileCollision(otherTile)
                Direction.LEFT -> tileTileCollision(otherTile)
                Direction.ROTATION -> tileTileCollision(otherTile)
                else -> TileStatus.SUCCESS
            }
        }else{
            if(parent.getIsPreservedForm()){
                parent.setIsGrounded(true)
            }else{
                this.isGrounded = true
            }
            TileStatus.COLLISION_WITH_TILE
        }
    }

    fun updateToPotentialPoint(){
        setPoint(potentialPoint)
        potentialPoint = Vec2Int(x = 0, y = 0)
    }

    fun prepareForShift(){
        super.setStatus(TileStatus.WAITING)
    }
    fun isWaitingForShift():Boolean{
        return super.getStatus() == TileStatus.WAITING
    }

    fun shift(direction: Direction): TileStatus {

        val shiftPoint: Vec2Int = when(direction){
            Direction.DOWN -> Vec2Int(x = 1, y = 0)
            Direction.RIGHT -> Vec2Int(x = 0, y = 1)
            Direction.LEFT -> Vec2Int(x = 0, y = -1)
            else -> Vec2Int(x = 0, y = 0)
        }
        super.setStatus(shift(direction, shiftPoint))
        return super.getStatus()
    }


    fun rotate(rotationMatrix:RotationMatrix):TileStatus {
        potentialPoint = (getPoint() + (rotationMatrix * getRelativePoint())) + getRelativePoint()
        return getTranslationStatus(Direction.ROTATION)
    }


    fun getRelativePoint():Vec2Int{
        return parent.getPivotTile().getPoint() - getPoint()
    }

    fun getIsPivot(): Boolean {
        return this.isPivot
    }
    fun setIsPivot(isPivot:Boolean){
        this.isPivot = isPivot
    }
}