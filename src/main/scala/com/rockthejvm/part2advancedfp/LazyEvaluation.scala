package com.rockthejvm.part2advancedfp

object LazyEvaluation {


  // lazy delays the evaluation of a value till its first use
  lazy val x: Int = {
    println("Hello")
    42
  }

  // call by need pattern = call by name + lazy values
  def byNameMethod(n: => Int): Int =
    n + n + n + 1

  def retrieveMagicValue() = {
    println("waiting...")
    Thread.sleep(1000)
    42
  }

  def demoByName(): Unit = {
    println(byNameMethod(retrieveMagicValue())) // 127
  }

  def byNeedMethod(n: => Int): Int = {
    lazy val lazyN = n // memoization

    lazyN + lazyN + lazyN + 1
  }

  def demoByNeed(): Unit = {
    println(byNeedMethod(retrieveMagicValue()))
  }


  // example with filter
  def lessThan30(i: Int): Boolean = {
    println(s"$i is less than 30?")
    i < 30
  }

  def greaterThan20(i: Int): Boolean = {
    println(s"$i is greater than 20?")
    i > 20
  }

  val numbers = List(1, 25, 40, 5, 23)

  def demoFilter(i: Int): Unit = {
    val lt30 = numbers.filter(lessThan30)
    val gt20 = lt30.filter(greaterThan20)
    println(gt20)
  }

  def demoWithFilter(): Unit = {
    val lt30 = numbers.withFilter(lessThan30)
    val gt20 = lt30.withFilter(greaterThan20)
    println(gt20.map(identity)) // x => x is called the identity functions (can also just put identity there)
  }

  def demoForComp(): Unit = {
    val forComp = for {
      n <- numbers if lessThan30(n) && greaterThan20(n)
    } yield n
    println(forComp)
  }


  def main(args: Array[String]): Unit = {
    demoByNeed()
    demoWithFilter()
    demoForComp()
  }

}
