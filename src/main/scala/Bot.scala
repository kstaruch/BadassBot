
import framework._
import BadAssBot.{MiniFoVBot, ExternalState, FoVBot}

class ControlFunctionFactory {

  def create = new ControlFunction().respond _
}

class ControlFunction {

  var globals = Map[String, String]()

  def respond(input: String): String = {
    val (opcode, params) = CommandParser.parse(input)

    opcode match {
      case "Welcome" =>
        globals = params
        ""
      case "React" =>

        try {

          val generation = params.get("generation").map(_.toInt).get
          val name = params.get("name").get
          val time = params.get("time").map(_.toInt).get
          val view = View(params.get("view").get)
          val energy = params.get("energy").map(_.toInt).get
          val master =  Coord.parse(params.getOrElse("master", "0:0"))
          val reloadCounter = params.getOrElse("reloadCounter", "0").toInt
          val previousMove =  Coord.parse(params.getOrElse(name + "_prevMove" , "0:0"))
          val internalStateSerialized = params.getOrElse("internalState", "")
          val apocalypse = globals.getOrElse("apocalypse", "0").toInt

          val externalState = ExternalState(
            generation, name, time, apocalypse, view, energy, master, previousMove, reloadCounter, internalStateSerialized
          )

          val bot = generation match {
            case 0 => new FoVBot()
            case _ => new MiniFoVBot()
          }

          bot.React(externalState).mkString("|")

        } catch {
          case e: Exception => {
            e.printStackTrace()
            ""
          }
        }

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

