package com.rockthejvm.part2advancedfp

import scala.annotation.targetName
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
  }


// exercise - is this a monad or not?
  case class PossiblyMonad[A](unsafeRun: () => A) {
    def map[B](f: A => B): PossiblyMonad[B] =
      PossiblyMonad(() => f(unsafeRun()))


    def flatMap[B](f: A => PossiblyMonad[B]): PossiblyMonad[B] =
      PossiblyMonad(() => f(unsafeRun()).unsafeRun())
  }

  object PossiblyMonad {
    @targetName("pure")
    def apply[A](value: => A): PossiblyMonad[A] =
      new PossiblyMonad(() => value)
  }

  // IT IS A MONAD!
  def possiblyMonadStory(): Unit = {
    val aPossiblyMonad = PossiblyMonad(42)
    val f = (x: Int) => PossiblyMonad(x + 1)
    val g = (x: Int) => PossiblyMonad(2 * x)
    val pure = (x: Int) => PossiblyMonad(x)

    // prop 1: left identity -> pure applied on a value + flatmap any function == function invoked on that value
    val prop1 = pure(42).flatMap(f) == f(42)

    // prop 2: right identity -> given a possible monad, flat mapping with pure == that possible monad
    val prop2 = aPossiblyMonad.flatMap(pure) == aPossiblyMonad

    // prop 3: associativity
    val prop3 = aPossiblyMonad.flatMap(f).flatMap(g) == aPossiblyMonad.flatMap(x => f(x).flatMap(g))

    // false negatives
    println(prop1)
    println(prop2)
    println(prop3)

    // real tests: values produced
    val prop1_1 = pure(42).flatMap(f).unsafeRun() == f(42).unsafeRun()
    val prop2_1 = aPossiblyMonad.flatMap(pure).unsafeRun() == aPossiblyMonad.unsafeRun()
    val prop3_1 = aPossiblyMonad.flatMap(f).flatMap(g).unsafeRun() == aPossiblyMonad.flatMap(x => f(x).flatMap(g)).unsafeRun()

    println(prop1_1)
    println(prop2_1)
    println(prop3_1)
  }

  // the values inside wont print if we run => the monad descirbes the computations but it wont perform them
  // side effects will not be performed => seperates the description of computation from performace of computation
  // that is why we have the unsafe run method.
  def possiblyMonadExample(): Unit = {
    val aPossiblyMonad = PossiblyMonad {
      println("doing some stuffs")
      42
    }

    val aPossiblyMonad2 = PossiblyMonad {
      println("doing some more stuffs") // doesnt print
      "Scala"
    }

    val aForComp = for {
      number <- aPossiblyMonad
      string <- aPossiblyMonad2
    } yield println(s"$number-$string")

    aPossiblyMonad.unsafeRun()
    aForComp // wont print
//    aForComp.unsafeRun() // will print but also prints everything that it calls :-/
  }

  // so we can do pure FP on any computations that might perform side effects
  // so we can chain or compose computations that might cause side effects
  // and at the end of app we can call unsafeRun on the final monad instance
  // unsafeRun sits at the bottom of libraries like ZIO or CATS
  // PossiblyMonad type is actually called IO!
  // this exercise is a vast simplification of the IO in CATS or ZIO


  def main(args: Array[String]): Unit = {
    possiblyMonadExample()

    possiblyMonadStory()
  }

}
