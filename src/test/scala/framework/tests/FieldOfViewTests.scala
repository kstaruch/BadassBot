package framework.tests

import org.scalatest.FunSuite
import framework.{Coord, FieldOfView}

class FieldOfViewTests extends FunSuite {

  test ("vector in view direction should be in FOV") {
    assert(FieldOfView.isIn45DegFieldOfView(Coord(0, 1), Coord(0, 50)))
  }

  test ("vector opposite view direction should not be in FOV") {
    assert(FieldOfView.isIn45DegFieldOfView(Coord(0, 1), Coord(0, -50)) === false)
  }

  test ("vector 90 deg to view direction should not be in FOV") {
    assert(FieldOfView.isIn45DegFieldOfView(Coord(0, 1), Coord(50, 0)) === false)
  }

  test ("vector 40 deg to view direction should not be in FOV") {
    assert(FieldOfView.isIn45DegFieldOfView(Coord(0, 1), Coord(50, 60)) === false)
  }

  test ("vector 20 deg to view direction should be in FOV") {
    assert(FieldOfView.isIn45DegFieldOfView(Coord(0, 1), Coord(50, 137)))
  }

}