package blogger.jscott2k.tetris.tetromino

import blogger.jscott2k.tetris.utils.Vec2Int

object Scheme {

    private const val P: Boolean = true //Tetromino Tile
    private const val X: Boolean = false //No Tile

    enum class TetrominoScheme(
        private val arrangement: TetrominoArrangement,
        private val charIdentifier: Char,
        private val pivotPoint: Vec2Int,
        private val maxRotationIndex:Int = 3,
        private val rotationAmount:Int = 90,
        private val calculateRotationDirection:(Int, Int)->Boolean = { dr:Int, _:Int -> (dr>0)}
    ) {
        I(
            TetrominoArrangement(
                configure =
                arrayOf(
                    booleanArrayOf(
                        P, P, P, P
                    ), booleanArrayOf(
                        X, X, X, X
                    ), booleanArrayOf(
                        X, X, X, X
                    ), booleanArrayOf(
                        X, X, X, X
                    )
                )
            ), charIdentifier = 'I', pivotPoint = Vec2Int(0, 1), maxRotationIndex = 2, rotationAmount = 90,
            calculateRotationDirection = { _:Int, index:Int -> (index==0)}
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
            charIdentifier = 'O', pivotPoint = Vec2Int(1, 0), maxRotationIndex = 1, rotationAmount = 0
        ),
        T(
            TetrominoArrangement(
                configure =
                arrayOf(
                    booleanArrayOf(
                        X, X, X, X
                    ), booleanArrayOf(
                        P, P, P, X
                    ), booleanArrayOf(
                        X, P, X, X
                    ), booleanArrayOf(
                        X, X, X, X
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
                        X, X, X, X
                    ), booleanArrayOf(
                        X, P, P, X
                    ), booleanArrayOf(
                        P, P, X, X
                    ), booleanArrayOf(
                        X, X, X, X
                    )
                )
            ),
            charIdentifier = 'S',
            pivotPoint = Vec2Int(2, 1)
        ),
        Z(
            TetrominoArrangement(
                configure =
                arrayOf(
                    booleanArrayOf(
                        X, X, X, X
                    ), booleanArrayOf(
                        P, P, X, X
                    ), booleanArrayOf(
                        X, P, P, X
                    ), booleanArrayOf(
                        X, X, X, X
                    )
                )
            ),
            charIdentifier = 'Z', pivotPoint = Vec2Int(2, 1)

        ),
        J(
            TetrominoArrangement(
                configure =
                arrayOf(
                    booleanArrayOf(
                        P, X, X, X
                    ), booleanArrayOf(
                        P, P, P, X
                    ), booleanArrayOf(
                        X, X, X, X
                    ), booleanArrayOf(
                        X, X, X, X
                    )
                )
            ),
            charIdentifier = 'J', pivotPoint = Vec2Int(1, 1)
        ),
        L(
            TetrominoArrangement(
                configure =
                arrayOf(
                    booleanArrayOf(
                        X, X, X, P
                    ), booleanArrayOf(
                        X, P, P, P
                    ), booleanArrayOf(
                        X, X, X, X
                    ), booleanArrayOf(
                        X, X, X, X
                    )
                )
            ),
            charIdentifier = 'L', pivotPoint = Vec2Int(1, 2)
        );

        companion object {

            private var allowedSchemes:MutableList<TetrominoScheme> = values().toMutableList()

            fun getRandom(): TetrominoScheme {
                return allowedSchemes.random()
            }
            fun setAllowedSchemes(allowedSchemes:List<TetrominoScheme>){
                println("SETTING ALLOWED TETROMINOS: $allowedSchemes")
                Companion.allowedSchemes = allowedSchemes.toMutableList()
            }
            fun removeAllowedScheme(scheme: TetrominoScheme){
                allowedSchemes.remove(scheme)
            }
            fun getAllowedSchemes():List<TetrominoScheme>{
                return allowedSchemes
            }
            fun setDefaultAllowedSchemes(){
                allowedSchemes = values().toMutableList()
            }
        }

        fun getCharIdentifier(): Char {
            return charIdentifier
        }

        fun getGridPoint(index: Int): Vec2Int {
            return arrangement.getAsOriginGridPoints()[index]
        }

        fun getPivotPoint(): Vec2Int {
            return pivotPoint
        }
        fun getMaxRotationIndex():Int{
            return maxRotationIndex
        }

        fun getRotationAmountAsRadians():Double{
            return Math.toRadians(this.rotationAmount.toDouble())
        }

        fun calculateRotationDirection(dr:Int, rotationIndex:Int):Boolean{
            return calculateRotationDirection.invoke(dr, rotationIndex)
        }
    }

}
