package blogger.jscott2k.tetris.enums
enum class ShiftStatus{
    OUTSIDE_COLUMN_LEFT,
    OUTSIDE_COLUMN_RIGHT,
    OUTSIDE_ROW_TOP,
    OUTSIDE_ROW_BOTTOM,
    SUCCESS,
    COLLISION_WITH_PIECE,
    GROUNDED
}