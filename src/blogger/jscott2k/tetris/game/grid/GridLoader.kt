package blogger.jscott2k.tetris.game.grid

import blogger.jscott2k.tetris.utils.Vec2Int
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.lang.Exception
import java.net.URI
import org.json.JSONString
import java.nio.charset.Charset

class GridLoader(private var level:Int = 1, private val rows:Int, private val cols:Int) {

    private var location:String = "/gridlayouts/$level.json"
    private var levelData:JSONArray? = null
    private val ROW_INDEX = 0
    private val COL_INDEX = 1
    init{
        load()
    }

    fun loadLevel(level:Int){
        this.level = level
        load()
    }

    fun loadNextLevel(){
        this.level++
        load()
    }
    private fun load(){
        location = "/gridlayouts/$level.json"
        var source:String?
        try{
           val resource: URI? = javaClass.getResource(location)?.toURI()
           if(resource!=null) {
               println("LOADING: $this, LEVEL: $level, FROM $resource AS RESOURCE")
               source = File(resource).readText(Charset.defaultCharset())
               setLevelData(source)
           }
        }catch(e:IllegalArgumentException){
            println("\tTROUBLE LOADING GRID $level AS RESOURCE... -> ${e.message}")
            println("\t\tATTEMPTING TO LOAD DIRECTLY FROM PATH .$location")
            source = File(".$location").readText(Charset.defaultCharset())
            setLevelData(source)
        }catch(e:Exception) {
            e.printStackTrace()
            return
        }
    }

    private fun setLevelData(source: String) {
        levelData = JSONArray(source)
        println("\tLEVEL DATA LOADED: $levelData")
    }

    fun getRows():Int{
        return this.rows
    }
    fun getCols():Int{
        return this.cols
    }

    fun getLevelData():List<Vec2Int>{

        val vecData:MutableList<Vec2Int> = mutableListOf()

        if(levelData==null){
            println("CANNOT RETRIEVE LEVEL DATA VECTORS: levelData=$levelData")
            return vecData
        }

        for(i:Int in 0 until levelData!!.length()){

            val vecJson:JSONArray? = levelData?.getJSONArray(i)

            val row:Int? = vecJson?.getInt(ROW_INDEX)
            val col:Int? =vecJson?.getInt(COL_INDEX)

            if(row == null || col == null){
                println("\t\tILLEGAL VECTOR (JSON) $vecJson AT $i of $levelData")
                break
            }
            val vecInt:Vec2Int = Vec2Int(row, col)
            println("CONVERTING LEVEL DATA (JSON) TO VEC -> $vecInt")
            vecData.add(vecInt)
        }
        return vecData
    }
}