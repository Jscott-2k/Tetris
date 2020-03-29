package blogger.jscott2k.tetris.tetromino
import blogger.jscott2k.tetris.utils.Vec2Int

const val P:Boolean = true
const val X:Boolean = false

    enum class TetrominoScheme(private val arrangement: TetrominoArrangement, private val charIdentifier: Char, private val pivotPoint: Vec2Int) {
        I(
            TetrominoArrangement(
                configure =
                arrayOf(
                    booleanArrayOf(
                        X, P, X, X
                    ), booleanArrayOf(
                        X, P, X, X
                    ), booleanArrayOf(
                        X, P, X, X
                    ), booleanArrayOf(
                        X, P, X, X
                    )
                )
            ), charIdentifier = 'I', pivotPoint = Vec2Int(2, 0)
        ),
        O(
            TetrominoArrangement(
                configure =
                arrayOf(
                    booleanArrayOf(
                        P, P, X, X
                    ), booleanArrayOf(
                        P, P, X, X
                    ), booleanArrayOf(
                        X, X, X, X
                    ), booleanArrayOf(
                        X, X, X, X
                    )
                )
            ),
            charIdentifier = 'O', pivotPoint = Vec2Int(1, 1)
        ),
        T(
            TetrominoArrangement(
                configure =
                arrayOf(
                    booleanArrayOf(
                        P, P, P, X
                    ), booleanArrayOf(
                        X, P, X, X
                    )
                )
            ),
            charIdentifier = 'T'
            , pivotPoint = Vec2Int(1, 1)
        ),
        S(
            TetrominoArrangement(
                configure =
                arrayOf(
                    booleanArrayOf(
                        X, P, P, X
                    ), booleanArrayOf(
                        P, P, X, X
                    ), booleanArrayOf(
                        X, X, X, X
                    ), booleanArrayOf(
                        X, X, X, X
                    )
                )
            ),
            charIdentifier = 'S',
            pivotPoint = Vec2Int(1, 1)
        ),
       Z(
           TetrominoArrangement(
               configure =
               arrayOf(
                   booleanArrayOf(
                       P, P, X, X
                   ), booleanArrayOf(
                       X, P, P, X
                   ), booleanArrayOf(
                       X, X, X, X
                   ), booleanArrayOf(
                       X, X, X, X
                   )
               )
           ),
           charIdentifier = 'Z', pivotPoint = Vec2Int(1, 1)

       ),
       J(
           TetrominoArrangement(
               configure =
               arrayOf(
                   booleanArrayOf(
                       X, P, X, X
                   ), booleanArrayOf(
                       X, P, X, X
                   ), booleanArrayOf(
                       P, P, X, X
                   ), booleanArrayOf(
                       X, X, X, X
                   )
               )
           ),
           charIdentifier = 'J',pivotPoint = Vec2Int(1, 1)

       ),
        L(
            TetrominoArrangement(
                configure =
                arrayOf(
                    booleanArrayOf(
                        P, X, X, X
                    ), booleanArrayOf(
                        P, X, X, X
                    ), booleanArrayOf(
                        P, P, X, X
                    ), booleanArrayOf(
                        X, X, X, X
                    )
                )
            ),
            charIdentifier = 'L',pivotPoint = Vec2Int(1, 0)
        );

        companion object {
            fun getRandom(): TetrominoScheme {
                return values().random()
            }
        }

        fun getCharIdentifier(): Char {
            return charIdentifier
        }
        fun getGridPoint(index:Int): Vec2Int {
           return arrangement.getAsOriginGridPoints()[index]
        }
        fun getPivotPoint():Vec2Int{
            return pivotPoint
        }

    }
