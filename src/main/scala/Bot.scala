
import framework._
import BadAssBot.{SimulationSettings, MiniFoVBot, ExternalState, FoVBot}

class ControlFunctionFactory {

  def create = new ControlFunction().respond _
}

class ControlFunction {

  var globals = Map[String, String]()
  var startTime = 0L

  def respond(input: String): String = {
    val (opcode, params) = CommandParser.parse(input)

    opcode match {
      case "Welcome" =>
        globals = params
        startTime = System.currentTimeMillis

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
          val apocalypse = globals.getOrElse("apocalypse", "0").toInt
          val maxSlaves = globals.getOrElse("maxslaves", Int.MaxValue.toString).toInt
          val slaves = params.getOrElse("slaves", "0").toInt
          val role = params.get("role")

          val externalState = ExternalState(
            generation, name, time, apocalypse,
            view, energy, master, previousMove,
            reloadCounter, maxSlaves, slaves, role, params
          )

          val bot = generation match {
            case 0 => {
              val settings = SimulationSettings(
                minionSpawnProbability = 0.8,
                enemyMinionSpawnDistance = 6,
                enemyMinionSpawnProbability = 0.2 * 1000 / energy,
                slideLength = 10,
                energyGoal = (energy / 50).max(200).min(2000),
                suicideRange = 3,
                suicideProbability = 0.3,
                roundsBeforeApocalypseSuicide = 5)
              new FoVBot(settings)
            }
            case _ => {
              val settings = SimulationSettings(
                minionSpawnProbability = 0.8,
                enemyMinionSpawnDistance = 3,
                enemyMinionSpawnProbability = 0.1 * 1000 / energy,
                slideLength = 10,
                energyGoal = (energy / 50).min(200).max(2000),
                suicideRange = 3,
                suicideProbability = 0.3,
                roundsBeforeApocalypseSuicide = 5)
              new MiniFoVBot(settings)
            }
          }

          bot.React(externalState).mkString("|")

        } catch {
          case e: Exception => {
            e.printStackTrace()
            ""
          }
        }
      case "Goodbye" =>
        val endTime = System.currentTimeMillis
        val round = globals("round").toInt
        val energy = params("energy").toInt

        println("BadassBot: round=" + round + ", score=" + energy + ", time=" + ((endTime - startTime) / 1000) + "s")
        ""
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

