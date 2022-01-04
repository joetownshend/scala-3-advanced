package com.rockthejvm.part3async

object JVMConcurrencyProblems {

  // vars are root of all evil when running in parallel
  def runInParallel(): Unit = {
    var x = 0 // one issue is mutable values = bad

    val thread1 = new Thread(() => {
      x = 1
    })

    val thread2 = new Thread(() => {
      x = 2
    })

    // start these at (roughly) the same time
    thread1.start()
    thread2.start()

    // if this method is run enough times eventually a scenario will be reached
    // where despite thread1 starting first, it ends last meaning that x = 1
    println(x)
    // this is called a race condition
  }

  case class BankAccount(var amount: Int)

  def buy(account: BankAccount, thing: String, price: Int): Unit = {
    account.amount -= price
  }

  def buySafe(account: BankAccount, thing: String, price: Int): Unit = {
    account.synchronized { // does not allow multiple theads to run critical section at same time :-)
      account.amount -=  price // critical section
    }
  }

  def demoBankingProblem(): Unit = {
    (1 to 10000).foreach {_ =>
      val account = BankAccount(50000)
      val thread1 = new Thread(() => buy(account, "shoes", 3000))
      val thread2 = new Thread(() => buy(account, "iPhone", 4000))

      thread1.start()
      thread2.start()
      thread1.join()
      thread2.join()

      if (account.amount != 43000) println(s"I've broken the account ${account.amount}")
    }
  }


  // exercise: inception threads TODO

  def inceptionThreads(maxThreads: Int, i: Int = 1): Thread =
    new Thread(() => {
      if (i < maxThreads) {
        val newThread = inceptionThreads(maxThreads, i + 1)
        newThread.start()
        newThread.join()
      }
      println(s"hello from thead ${i}")
    })

  def main(args: Array[String]): Unit = {
    inceptionThreads(50).start()
  }
}
