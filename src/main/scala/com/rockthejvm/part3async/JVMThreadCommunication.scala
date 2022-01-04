package com.rockthejvm.part3async

import scala.collection.mutable
import scala.util.Random

object JVMThreadCommunication {
  def main(args: Array[String]): Unit = {
    ProdConsV2.start()
  }
}

// problem: the producer-consumer problem
class SimpleContainer {
  private var value: Int = 0

  def isEmpty: Boolean = value == 0
  def set(newValue: Int): Unit =
    value = newValue

  def get: Int = {
    val result = value
    value = 0
    result
  }

  def getInt: Int = {
    val result = value
    value = 0
    result
  }
}

// PC part 1: one producer, one comsumer
object ProdConsV1 {
  def start(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting...")

      container.synchronized { // block all other threads "locking" object
        // thread safe code
        container.wait() // release the lock + suspend the thread
        // reacquire the lock here
        // continue execution
      }

      println(s"[consumer] I have consumed a value ${container.getInt}")
    })

    val producer = new Thread(() => {
      println("[prodcuer] computing...")
      Thread.sleep(500)
      val value = 42

      container.synchronized {
        println(s"I am producing - ${value}")
        container.set(value)
        container.notify() // awake ONE suspended thread on this obj
      } // release the lock
    })
    consumer.start()
    producer.start()
  }
}

// PC part 2: one producer, one comsumer
object ProdConsV2 {
  def start(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting...")
      while (container.isEmpty) {
        println("[consumer] waiting for a value...")
      }
      println(s"[consumer] I have consumed a value ${container.getInt}")
    })

    val producer = new Thread(() => {
      println("[prodcuer] computing...")
      Thread.sleep(500)
      val value = 42
      println(s"[producer] I am producing after long work the value ${value}")
      container.set(value)
    })
    consumer.start()
    producer.start()
  }
}