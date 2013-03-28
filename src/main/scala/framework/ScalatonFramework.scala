package framework

import util.Random

// -------------------------------------------------------------------------------------------------
// Framework
// -------------------------------------------------------------------------------------------------


sealed trait MiniOp
sealed trait Op extends MiniOp

/**
 * Move(direction=int:int)
 * Moves the bot one cell in a given direction, if possible. The delta values are signed integers. The permitted values are -1, 0 or 1.
 *
 * Parameters:
 * direction desired displacement for the move, e.g. 1:1 or 0:-1
 *
 * Example:
 * Move(direction=-1:1) moves the entity left and down.
 *
 * Energy Cost/Permissions:
 * for master bot: 0 EU (free)
 * for mini-bot: 0 EU (free)
 */
case class Move(direction: Heading) extends Op {
  override def toString = "Move(direction=%s)".format(direction)
}

/**
 * Spawn(direction=int:int,name=string,energy=int,…)
 * Spawns a mini-bot from the position of the current entity at the given cell position, expressed relative to the current position.
 *
 * Parameters:
 *
 * direction: desired displacement for the spawned mini-bot, e.g. -1:1
 * name: arbitrary string, except the following characters are not permitted: |, ,, =, (
 * energy: energy budget to transfer to the spawned mini-bot (minimum: 100 EU)
 *
 * Defaults:
 * name = Slave_nn an auto-generated unique slave name
 * energy = 100 the minimum permissible energy
 *
 * Additional Parameters:
 * In addition to the parameters listed above, the command can contain arbitrary additional parameter key/value pairs. These will be set as the initial state parameters of the entity and will be passed along to all subsequent control function invocations with React. This allows a master bot to “program” a mini-bot with arbitrary starting parameters.
 * The usual restrictions for strings apply (no comma, parentheses, equals sign or pipe characters).
 * The following property names are reserved and must not be used for custom properties: generation, name, energy, time, view, direction, master.
 * Properties whose values are empty strings are ignored.
 * Example:
 *
 * Spawn(direction=-1:1,energy=100) spawns a new mini-bot one cell to the left and one cell down, with an initial energy of 100 EU.
 *
 * Energy Cost/Permissions:
 * for master bot: as allocated via energy
 * for mini-bot: as allocated via energy
 *
 * Note that this means that mini-bots can spawn other mini-bots (if they have the required energy, i.e. at least 100 EU).
 */
case class Spawn(direction: Heading, name: Option[String] = None, energy: Int) extends Op {
  require(energy >= 100, "energy must be >= than 100")
  override def toString =
    Util.string("Spawn", "direction" -> direction, "energy" -> energy, "name" -> name)
}

/**
 * Set(key=value,…)
 * Sets one or more state parameters with the given names to the given values. The state parameters of the entity will be passed along to all subsequent control function invocations with React. This allows an entity to store state information on the server, making its implementation immutable and delegating state maintenance to the server.
 *
 * The usual restrictions for strings apply (no comma, parentheses, equals sign or pipe characters).
 *
 * The following property names are reserved and must not be used for custom properties: generation, name, energy, time, view, direction, master.
 * Properties whose values are empty strings are deleted from the state properties.
 *
 * No Energy Cost/ All bots are permitted.
 */
case class Set(map: Map[String, String]) extends Op {
  override def toString = Util.string("Set", map.toSeq: _*)
}

//
// simulation neutral
//

/**
 * Say(text=string)
 * Displays a little text bubble that remains at the position where it was created. Use this to drop textual breadcrumbs associated with events. You can also use this as a debugging tool. Don't go overboard with this, it'll eventually slow down the gameplay.
 *
 * Parameters:
 * text the message to display; maximum length: 10 chars; can be an arbitrary string, except the following characters are not permitted: |, ,, =, (
 *
 * Energy Cost/Permissions:
 * for master bot: permitted, no energy consumed
 * for mini-bot: permitted, no energy consumed
 */
case class Say(text: String) extends Op {
  override def toString = Util.string("Say", "text" -> text)
}

/**
 * Status(text=string)
 * Shortcut for setting the state property 'status', which displays a little text bubble near the entity which moves around with the entity. Use this to tell spectators about what your bot thinks. You can also use this as a debugging tool. If you return the opcode Status, do not also set the status property via Set, since no particular order of execution is guaranteed.
 *
 * Parameters:
 * text the message to display; maximum length: 20 chars; can be an arbitrary string, except the following characters are not permitted: |, ,, =, (
 *
 * Energy Cost/Permissions:
 * for master bot: permitted, no energy consumed
 * for mini-bot: permitted, no energy consumed
 */
case class Status(text: String) extends Op {
  override def toString = Util.string("Status", "text" -> text)
}

/**
 * MarkCell(position=int:int,color=string)
 * Displays a cell as marked. You can use this as a debugging tool.
 *
 * Parameters:
 * position desired displacement relative to the current bot, e.g. -2:4 (defaults to 0:0)
 * color color to use for marking the cell, using HTML color notation, e.g. #ff8800 (default: #8888ff)
 *
 * Energy Cost/Permissions:
 * for master bot: permitted, no energy consumed
 * for mini-bot: permitted, no energy consumed
 */
case class MarkCell(position: Heading, color: Color) extends Op {
  override def toString = Util.string("MarkCell", "position" -> position, "color" -> color)
}

/**
 * DrawLine(from=int:int,to=int:int,color=string)
 * Draws a line. You can use this as a debugging tool.
 *
 * Parameters:
 * from starting cell of the line to draw, e.g. -2:4 (defaults to 0:0)
 * to destination cell of the line to draw, e.g. 3:-2 (defaults to 0:0)
 * color color to use for marking the cell, using HTML color notation, e.g. #ff8800 (default: #8888ff)
 *
 * Energy Cost/Permissions:
 * for master bot: permitted, no energy consumed
 * for mini-bot: permitted, no energy consumed
 */
case class DrawLine(from: Heading, to: Heading, color: Color) extends Op {
  override def toString = Util.string("DrawLine", "from" -> from, "to" -> to, "color" -> color)
}

/**
 * Log(text=string)
 * Shortcut for setting the state property debug, which by convention contains an optional (multi-line) string with debug information related to the entity that issues this opcode. This text string can be displayed in the browser-based debug window to track what a bot or mini-bot is doing. The debug information is erased each time before the control function is called, so there is no need to set it to an empty string.
 *
 * Parameters:
 * text the debug message to store. The usual restrictions for string values apply (no commas, parentheses, equals signs or pipe characters). Newline characters are permitted, however.
 *
 * Energy Cost/Permissions:
 * for master bot: permitted, no energy consumed
 * for mini-bot: permitted, no energy consumed
 */
case class Log(text: String) extends Op {
  override def toString = Util.string("Log", "text" -> text)
}

/**
 * Explode(size=int)
 * Detonates the mini-bot, dissipating its energy over some blast radius and damaging nearby entities. The mini-bot disappears. Parameters:
 *
 * size an integer value 2 < x < 10 indicating the desired blast radius
 *
 * Energy Cost/Permissions:
 * for master bot: cannot explode itself
 * for mini-bot: entire stored energy
 */
case class Explode(size: Int) extends MiniOp {
  require(2 < size && size < 10)
  override def toString = Util.string("Explode", "size" -> size)
}

object NoOp {
  def apply() = new NoOp
}

class NoOp extends MiniOp {
  override def toString = Util.string("Log", "text" -> "NoOp")
}

object Color {
  private def req(i: Int): Boolean = {require(i < 256 && i >= 0); true}

  //TODO define color constants
}

case class Color(r: Int, g: Int, b: Int) {
  Color.req(r) && Color.req(g) && Color.req(b)
  override def toString = "#" + r.toHexString + g.toHexString + b.toHexString
}

sealed trait Cell
object Cell {
  case object Unknown extends Cell
  case object Empty extends Cell
  case object Wall extends Cell
  case object You extends Cell
  case object Enemy extends Cell
  case object YourSlave extends Cell
  case object EnemySlave extends Cell
  case object GoodPlant extends Cell //
  case object BadPlant extends Cell
  case object GoodBeast extends Cell
  case object BadBeast extends Cell

  def apply(c: Char): Cell = c match {
    case '?' => Unknown
    case '_' => Empty
    case 'W' => Wall
    case 'M' => You
    case 'm' => Enemy
    case 'S' => YourSlave
    case 's' => EnemySlave
    case 'P' => GoodPlant
    case 'p' => BadPlant
    case 'B' => GoodBeast
    case 'b' => BadBeast
  }

}

sealed trait Displacement { def value: Int }
object Displacement {
  case object Neg extends Displacement { val value = -1 }
  case object Zero extends Displacement { val value = 0 }
  case object Pos extends Displacement { val value = 1 }

  def apply(in: Int) = in match {
    case -1 => Neg
    case 0  => Zero
    case 1  => Pos
  }
}

object Util {
  def parse[A](s: String)(f: (Int, Int) => A): A = {
    val a = s.split(':')
    f(a(0).toInt, a(1).toInt)
  }

  def string(name: String, is: (String, Any)*): String = {
    val w = new java.io.StringWriter().append(name).append("(")

    is.foreach {
      case (n, None)    =>
      case (n, Some(v)) => w.append(n).append("=").append(v.toString).append(",")
      case (n, v)       => w.append(n).append("=").append(v.toString).append(",")
    }

    w.append(")").toString
  }
}

object Heading {
  /** parse a value from Heading.toString format, e.g. "0:1". */
  def parse(s: String): Heading = Util.parse(s)(Heading.apply)
  def apply(x: Int, y: Int): Heading =
    if (x == 1) {
      if (y == 1) NorthEast
      else if (y == 0) East
      else SouthEast
    } else if (x == 0) {
      if (y == 1) North
      else if (y == 0) Nowhere
      else South
    } else {
      if (y == 1) NorthWest
      else if (y == 0) West
      else SouthWest
    }

  def random: Heading = {
    apply(Random.nextInt(3) - 1, Random.nextInt(3) - 1)
  }


  import Displacement.{ Pos, Zero, Neg }
  val East = new Heading(Pos, Zero)
  val NorthEast = Heading(Pos, Neg)
  val North = Heading(Zero, Neg)
  val NorthWest = Heading(Neg, Neg)
  val West = Heading(Neg, Zero)
  val SouthWest = Heading(Neg, Pos)
  val South = Heading(Zero, Pos)
  val SouthEast = Heading(Pos, Pos)

  val Nowhere = Heading(Zero, Zero)
}

case class Heading private (x: Displacement, y: Displacement) {
  override def toString = x.value + ":" + y.value
}

object Coord {
  /** parse a value from Coord.toString format, e.g. "0:1". */
  def parse(s: String): Coord = Util.parse(s)(Coord.apply)
  def apply(h: Heading): Coord = Coord(h.x.value, h.y.value)
}

case class Coord(x: Int, y: Int) {

  override def toString = x + ":" + y

  def toHeading: Heading = Heading(x, y)
  def isNonZero = x != 0 || y != 0
  def isZero = x == 0 && y == 0
  def isNonNegative = x >= 0 && y >= 0

  def updateX(newX: Int) = Coord(newX, y)
  def updateY(newY: Int) = Coord(x, newY)

  def addToX(dx: Int) = Coord(x + dx, y)
  def addToY(dy: Int) = Coord(x, y + dy)

  def +(pos: Coord) = Coord(x + pos.x, y + pos.y)
  def -(pos: Coord) = Coord(x - pos.x, y - pos.y)
  def *(factor: Double) = Coord((x * factor).intValue, (y * factor).intValue)

  def distanceTo(pos: Coord): Double = (this - pos).length // Phythagorean
  def length: Double = math.sqrt(x * x + y * y) // Phythagorean

  def stepsTo(pos: Coord): Int = (this - pos).stepCount // steps to reach pos: max delta X or Y
  def stepCount: Int = x.abs.max(y.abs) // steps from (0,0) to get here: max X or Y

  def signum = Coord(x.signum, y.signum)

  def wrap(size: Coord) = {
    def fix(a: Int, len: Int) = if (a < 0) len + a else if (a >= len) a % len else a
    val (xx, yy) = (fix(x, size.x), fix(y, size.y))
    if (xx != x || yy != y) Coord(xx, yy) else this
  }

  def toDouble = CoordDouble(x.toDouble, y.toDouble)
}

case class CoordDouble(x: Double, y: Double) {
  val length: Double = math.sqrt(x * x + y * y)
  def normalize: CoordDouble = CoordDouble(x / length, y / length)
  def scalarProduct(other: CoordDouble): Double = (x * other.x) + (y * other.y)
}

object FieldOfView {
  def isIn45DegFieldOfView(viewDirection: Coord, point: Coord) : Boolean = {
    val angle = math.acos(viewDirection.toDouble.normalize.scalarProduct(point.toDouble.normalize))
    val fieldOfView = math.Pi / 4
    angle <= fieldOfView / 2
  }
}

object View {
  trait Projection extends (Coord => Cell) {
    def indexFrom(c: Coord): Int
    def fromAbsolute(c: Coord): Coord
    def fromRelative(c: Coord): Coord
    def fromIndex(index: Int): Coord
  }
}

case class View(cells: String) extends (Coord => Cell) {

  val size = math.sqrt(cells.length).toInt
  val center = Coord(size / 2, size / 2)

  def apply(c: Coord): Cell = Relative(c)

  def nearestEnemyInRange(range: Int): Option[Coord] = {

    val enemyOffsets = offsetToNearest(Cell.Enemy) :: offsetToNearest(Cell.BadBeast) ::
      offsetToNearest(Cell.EnemySlave) :: Nil

    enemyOffsets.flatten.filter(_.length <= range) match {
      case Nil => None
      case x => Some(Absolute.fromRelative(x.minBy(_.length)))
    }
  }

  def slideIfNeeded(heading: Heading): Heading = {
    val candidate = Absolute.fromRelative(Coord(heading.x.value, heading.y.value)) //TODO: simplify
    canBeTraversed(candidate) match {
      case true => heading
      case false => slideIfNeeded(rotateClockwise(heading))
    }
  }

  def rotateClockwise(heading: Heading): Heading = {
    heading match {
      case Heading.North => Heading.NorthEast
      case Heading.NorthEast => Heading.East
      case Heading.East => Heading.SouthEast
      case Heading.SouthEast => Heading.South
      case Heading.South => Heading.SouthWest
      case Heading.SouthWest => Heading.West
      case Heading.West => Heading.NorthWest
      case Heading.NorthWest => Heading.North
      case _ => heading
    }
  }

  //TODO: refactor
  def chooseValidRandomHeading: Heading = {

    val candidate = Heading.random

    canBeTraversed(center + Coord(candidate)) match {
      case true => candidate
      case false => chooseValidRandomHeading
    }
  }

  //TODO: refactor to immutable and recursive; optimise - it can be precomputed
  def coordsInDirectionOf(heading: Heading): Seq[Coord] = {

    var result: List[Coord] = Nil
    val offs = size / 2
    for (
      x <- -offs to offs;
      y <- -offs to offs
      if !(x == 0 && y == 0))
      if (FieldOfView.isIn45DegFieldOfView(Coord(heading.x.value, heading.y.value), Coord(x, y))) {
        val relative = Coord(x, y)
        val absolute = Absolute.fromRelative(relative)
        result = absolute :: result
      }
      result
  }

  def canBeTraversed(coord: Coord): Boolean = {
    Absolute(coord) match {
      case Cell.Wall => false
      case Cell.BadBeast => false
      case Cell.Enemy => false
      case Cell.BadPlant => false
      case _ => true
    }
  }

  def validNeighborsOf(c: Coord): Seq[Coord] = {
    validNeighborsOf(c, 1)
  }

  //TODO: refactor to recursive
  def validNeighborsOf(position: Coord, r: Int): Seq[Coord] = {

    var neighbors: Seq[Coord] = Nil

    for (
      x <- -r to r;
      y <- -r to r
      if !(x == 0 && y == 0)) {
        val potentialNeighbor = position.addToX(x).addToY(y)

        if (potentialNeighbor.x < size && potentialNeighbor.y < size &&
            potentialNeighbor.x >= 0 && potentialNeighbor.y >= 0 &&
            canBeTraversed(potentialNeighbor)) {

          neighbors = potentialNeighbor +: neighbors
        }

    }

    neighbors
  }

  def offsetToNearest(c: Cell): Option[Coord] =
    cells.view.zipWithIndex.filter(ce => Cell(ce._1) == c) match {
      case Nil => None
      case x => Some(x.map(p => Relative.fromIndex(p._2)).minBy(_.length))
    }

  object Relative extends View.Projection {
    def indexFrom(c: Coord) = Absolute.indexFrom(Absolute.fromRelative(c))
    def fromAbsolute(c: Coord) = c - center
    def fromIndex(index: Int) = fromAbsolute(Absolute.fromIndex(index))
    def fromRelative(c: Coord) = c
    def apply(c: Coord) = Cell(cells.charAt(indexFrom(c)))
  }

  object Absolute extends View.Projection {
    def indexFrom(c: Coord) = c.x + c.y * size
    def fromIndex(index: Int) = Coord(index % size, index / size)
    def fromRelative(c: Coord) = c + center
    def fromAbsolute(c: Coord) = c
    def apply(c: Coord) = Cell(cells.charAt(indexFrom(c)))
  }
}
