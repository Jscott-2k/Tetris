package blogger.jscott2k.tetris.tetromino

import blogger.jscott2k.tetris.enums.Direction
import blogger.jscott2k.tetris.game.GameGrid
import blogger.jscott2k.tetris.utils.Vec2Int
import blogger.jscott2k.tetris.enums.TileStatus
import blogger.jscott2k.tetris.utils.RotationMatrix

class TetrominoTile(val parent: Tetromino, val grid: GameGrid){

    private var point: Vec2Int = Vec2Int(x = 0, y = 0)
    private var potentialPoint: Vec2Int = Vec2Int(x = 0, y = 0)
    private var isPivot:Boolean = false
    private var isGrounded:Boolean = false
    private var tileStatus:TileStatus = TileStatus.WAITING

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

    private fun getTranslationStatus(direction: Direction):TileStatus {
        val potentialCollidedTile: TetrominoTile? = grid.getTileAtPoint(potentialPoint)

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

//        if(parent.getIsGrounded() && parent.getIsPreservedForm()){
//            return TileStatus.GROUNDED_WITH_FORM_PRESERVED
//        }else if((!parent.getIsPreservedForm()) && (this.isGrounded)){
//            return TileStatus.GROUNDED_WITH_FORM_NOT_PRESERVED
//        }
//        else if(potentialPoint.y >= grid.getCols() ){
//            return TileStatus.OUTSIDE_COLUMN_RIGHT
//        }
//        else if(potentialPoint.y < 0){
//            return TileStatus.OUTSIDE_COLUMN_LEFT
//        }
//        else if(potentialPoint.x >= grid.getRows()){
//            this.onTileBottomRowCollision()
//            return TileStatus.OUTSIDE_ROW_BOTTOM
//        }
//        else if(potentialPoint.x < 0){
//            return TileStatus.OUTSIDE_ROW_TOP
//        }
//        else if(potentialCollidedTile!=null){
//            return this.onTileTileCollision(direction, potentialCollidedTile)
//        }
//        else{
//            return TileStatus.SUCCESS
//        }
//    }
    }
    private fun shift(direction: Direction, shiftPoint: Vec2Int): TileStatus {

        potentialPoint = (this.point + shiftPoint)
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

    private fun onTileTileCollision(direction:Direction, otherTile:TetrominoTile):TileStatus{

        return when(direction){
            Direction.DOWN -> downTileTileCollision(otherTile)
            Direction.RIGHT -> tileTileCollision(otherTile)
            Direction.LEFT -> tileTileCollision(otherTile)
            else -> TileStatus.SUCCESS
        }
    }

    fun updateToPotentialPoint(){
        setPoint(potentialPoint)
        potentialPoint = Vec2Int(x = 0, y = 0)
    }

    fun prepareForShift(){
        this.tileStatus = TileStatus.WAITING
    }
    fun isWaitingForShift():Boolean{
        return tileStatus == TileStatus.WAITING
    }

    fun shift(direction: Direction): TileStatus {

        val shiftPoint: Vec2Int = when(direction){
            Direction.DOWN -> Vec2Int(x = 1, y = 0)
            Direction.RIGHT -> Vec2Int(x = 0, y = 1)
            Direction.LEFT -> Vec2Int(x = 0, y = -1)
            else -> Vec2Int(x = 0, y = 0)
        }
        tileStatus = shift(direction, shiftPoint)
        return tileStatus
    }

    fun rotate(rotation:RotationMatrix):TileStatus {
        println("REL POS: ${getRelativePosition()}")
        potentialPoint = (this.getPoint() + (rotation * getRelativePosition())) + this.getRelativePosition()
        return getTranslationStatus(Direction.ROTATION)
    }

    fun getRelativePosition():Vec2Int{
        return (parent.getPivotTile().getPoint() - this.getPoint())
    }

    fun getIsPivot(): Boolean {
        return this.isPivot
    }
    fun setIsPivot(isPivot:Boolean){
        this.isPivot = isPivot
    }
}