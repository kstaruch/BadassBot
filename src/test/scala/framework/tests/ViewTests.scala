package framework.tests

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import framework._
import scala.Some
import util.parsing.json.JSON
import reflect.BeanInfo
import BadAssBot.StatePersister.BotStatePersister


class ViewTests  extends FunSpec with ShouldMatchers {

  describe("View") {

    it("calculates offset of nearest enemy correctly") {

      val v: String =
        "__P__" +
        "mWWW_" +
        "__M_b" +
        "_____" +
        "_____"

      val view: View = View(v)

      view.offsetToNearest(Cell.BadBeast) should equal(Some(Coord(2, 0)))


      /*
      val h1 = Heading(-1,1)

      val t1 = h1.x.value

      val t2 = h1.y.value

      val s = h1.toString

      s
        */

    }

    it("calculates None nearest enemy offset correctly") {

      val v: String =
        "__P__" +
          "mWWW_" +
          "__M_b" +
          "_____" +
          "_____"

      val view: View = View(v)

      view.offsetToNearest(Cell.EnemySlave) should equal(None)
    }

  }

}

