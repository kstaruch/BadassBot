
import framework._
import BadAssBot.{MiniFoVBot, ExternalState, FoVBot}

class ControlFunctionFactory {

  def create = (input: String) => {
    val (opcode, params) = CommandParser.parse(input)

    opcode match {
      case "React" =>

        val generation = params.get("generation").map(_.toInt).get
        val name = params.get("name").get
        val time = params.get("time").map(_.toInt).get
        val view = View(params.get("view").get)
        val energy = params.get("energy").map(_.toInt).get
        val master =  Coord.parse(params.getOrElse("master", "0:0"))
        val reloadCounter = params.getOrElse("reloadCounter", "0").toInt
        val previousMove =  Coord.parse(params.getOrElse(name + "_prevMove" , "0:0"))

        val externalState = ExternalState(
          generation, name, time, view, energy, master, previousMove, reloadCounter
        )

        val bot = generation match {
          case 0 => new FoVBot()
          case _ => new MiniFoVBot()
        }

        bot.React(externalState).mkString("|")

      case _ => ""
    }

  }
}


/** Utility methods for parsing strings containing a single command of the format
  * "Command(key=value,key=value,...)"
  */
object CommandParser {
  /** "Command(..)" => ("Command", Map( ("key" -> "value"), ("key" -> "value"), ..}) */
  def parse(command: String): (String, Map[String, String]) = {
    /** "key=value" => ("key","value") */
    def splitParameterIntoKeyValue(param: String): (String, String) = {
      val segments = param.split('=')
      (segments(0), if(segments.length>=2) segments(1) else "")
    }

    val segments = command.split('(')
    if( segments.length != 2 )
      throw new IllegalStateException("invalid command: " + command)
    val opcode = segments(0)
    val params = segments(1).dropRight(1).split(',')
    val keyValuePairs = params.map(splitParameterIntoKeyValue).toMap
    (opcode, keyValuePairs)
  }
}
