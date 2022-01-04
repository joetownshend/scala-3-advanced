package com.rockthejvm.part3async

import java.util.concurrent.Executors

object JVMConcurrencyIntro {

  def basicThreads(): Unit = {
    val runnable = new Runnable {
      override def run(): Unit = println("running on some thread")
    }

    // threads on JVM are denoted by Thread data type
    val aThread = new Thread(runnable)
    aThread.start() // runs runnable on some JVM thread
    // JVM thread == OS thread (soon to change via project Loom)
    aThread.join() // block until thread finishes
  }

  // order of operations is NOT guaranteed
  def OrderOfOperations(): Unit = {
    val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))
    val threadGoodbye = new Thread(() => (1 to 5).foreach(_ => println("goodbye")))

    threadHello.start()
    threadGoodbye.start()
  }

  // executors
  def demoExecutors(): Unit = {
    val threadPool = Executors.newFixedThreadPool(4)
    // submit a computation
    threadPool.execute(() => println("Something in the thread pool"))
    // close computation
    threadPool.shutdown()
  }


  def main(args: Array[String]): Unit = {
    OrderOfOperations()
  }
}
