package blogger.jscott2k.tetris.game

import java.lang.IndexOutOfBoundsException


object InputManager {

    var inputActionMap: MutableMap<String, (args:List<String>) -> Unit> = mutableMapOf()

    fun invoke(input: String) {

        val inputs: List<String> = input.split(Regex("\\s"))
        val params: List<String> = if(inputs.size > 1) {inputs.subList(1, inputs.size)} else{ emptyList()}
        val key:String = inputs[0]

        try{
            inputActionMap[key]?.invoke(params)
        }catch (e:IndexOutOfBoundsException){
            println("Error: Invalid Command Argument(s). Maybe you are missing something?")
        }
    }
    fun addInputAction(key: String, action: (args:List<String>) -> Unit) {
        inputActionMap.putIfAbsent(key, action)
    }
}