package com.rockthejvm.part3async

import java.util.concurrent.Executors
import scala.concurrent.{Await, ExecutionContext, Future, Promise}
import scala.util.{Failure, Random, Success, Try}
import scala.concurrent.duration.*

object Futures {

  def calculateMeaningOfLife(): Int = {
    // simulate a long compute
    Thread.sleep(1000)
    42
  }

  // Thread pool - Java specific
  val executor = Executors.newFixedThreadPool(4)
  // Thread pool - Scala specific
  given executionContext: ExecutionContext = ExecutionContext.fromExecutorService(executor)

  // a future is an async computation that will finish at some point
  val aFuture: Future[Int] = Future.apply(calculateMeaningOfLife()) // given execContext will be passed here

  // Option[Try[Int]]
  // 1. we dont know if we have a value
  // 2. if we do, it could be a failed computation
  val futureInstantResult: Option[Try[Int]] = aFuture.value

  // callbacks
  aFuture.onComplete {
    case Success(value) => println(s"I've completeed with the meaning of life ${value}")
    case Failure(ex) => println(s"My async computation failed ${ex}")

  }

  def main(args: Array[String]): Unit = {
    println(futureInstantResult) // inspect value of future RIGHT NOW
    Thread.sleep(2000)
    executor.shutdown()
  }
}
