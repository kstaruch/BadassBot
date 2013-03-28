package framework.tests

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import framework._
import scala.Some


class CommandParserTests  extends FunSpec with ShouldMatchers {

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
