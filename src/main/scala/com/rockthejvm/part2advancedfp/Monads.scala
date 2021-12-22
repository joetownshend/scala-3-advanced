package com.rockthejvm.part2advancedfp

object Monads {

  val alist = List(1,2,3)

  def listStory(): Unit = {
    val listMultiply = for {
      x <- List(1,2,3)
      y <- List(4,5,6)
    } yield x * y

    // alt syntax
    val listMultiplyExpand = List(1,2,3).flatMap(x => List(4,5,6).map(y => x * y))

    val f = (x: Int) => List(x, x + 1)
    val g = (x: Int) => List(x, 2 * x)
    val pure = (x: Int) => List(x) // same as list constructor

    // prop 1: left identity
    val leftIdentity = pure(42).flatMap(f) == f(42)

    // prop 2: right identity
    val rightIdentity = alist.flatMap(pure)

    // prop 3: associativity
    val associativity = alist.flatMap(f).flatMap(g)
  }

  def optionStory(): Unit = {
    val anOption = Option(42)
    val optionString = for {
      lang <- Option("Scala")
      ver <- Option(3)
    } yield s"$lang-$ver"

    val f = (x: Int) => Option(x + 1)
    val g = (x: Int) => Option(2 * x)
    val pure = (x: Int) => Option(x)

    // prop 1: left-identity
    val leftIdentity = pure(42).flatMap(f) == f(42) // true

    // prop 2: right-identity
    val rightIdentity = anOption.flatMap(pure) == anOption // true

    // prop 3: associativity
    val associativity = anOption.flatMap(f).flatMap(g) == anOption.flatMap(x => f(x).flatMap(g))

    def main(args: Array[String]): Unit = {

    }


  }


  def main(args: Array[String]): Unit = {

  }

}
