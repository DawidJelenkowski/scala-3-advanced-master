package com.rockthejvm.part1as

import scala.util.Try
object DarkSugars {

  // 1- sugar for methods with one argument
  def singleArgMethod(arg: Int): Int = arg + 1

  val aMethodCall = singleArgMethod({
    // long code
    42
  })

  val aMethodCall_v2 = singleArgMethod {
    // long code
    42
  }

  // example: Try, Future
  val aTryInstance = Try {
    throw new RuntimeException
  }

  // with hofs
  val anIncrementedList = List(1,2,3).map { x =>
    // code block
    x + 1
  }

  // 2- single abstract method pattern
  trait Action {
    // can also have other implemented fields/methods here
    def act(x: Int): Int
  }

  val anAction = new Action {
    override def act(x: Int): Int = x + 1
  }

  val anotherAction: Action = (x: Int) => x + 1 // new Action { override def act(x: Int): Int = x + 1 }
  println(anotherAction.act(42))

  // example: Runnable
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Hi, Scala, from another thread")
  })

  val aSweeterThread = new Thread(() => println("Hi, Scala"))


  def main(args: Array[String]): Unit = {

  }
}
