package blogger.jscott2k.tetris.game

import java.lang.Exception
import java.lang.NullPointerException

object InputManager {

    var inputActionMap: MutableMap<String, (args:List<String>) -> Boolean> = mutableMapOf()

    fun invoke(input: String): Boolean {

        val inputs: List<String> = input.split(Regex(pattern = "\\s+"))
        val params: List<String> = if(inputs.size > 1) {inputs.subList(1, inputs.size)} else{ emptyList()}
        val key:String = inputs[0]

        println("Processing Command: {command: $key, params: $params}")

         return try{
             inputActionMap[key]!!.invoke(params)
         }catch(e:NullPointerException){
             println("Error: Unknown command {command: $key, params: $params}")
             println("\tDetails: ${e.message}")
             true
         }catch (e:Exception){
             println("Error: An error occurred processing {command: $key, params: $params}")
             println("\tDetails: ${e.message}")
             true
         }

    }

    fun addInputAction(key: String, action: (args:List<String>) -> Boolean) {
        inputActionMap.putIfAbsent(key, action)
    }
}