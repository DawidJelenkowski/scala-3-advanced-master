error id: file://<WORKSPACE>/src/main/scala/com/rockthejvm/part1as/DarkSugars.scala:[1503..1507) in Input.VirtualFile("file://<WORKSPACE>/src/main/scala/com/rockthejvm/part1as/DarkSugars.scala", "package com.rockthejvm.part1as

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
  val anIncrementedList = List(1,2,3).map {x =>
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

  val anotherAction: Action = (x: Int) => x + 1 // new Action {override def act(x: Int): Int = x + 1}
  println(anotherAction.act(42))

  // example: Runnable
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Hi, Scala, from another thread")
  })

  val aSweeterThread = new Thread(() => println("Hi, Scala"))

  // 3 - methods ending in a : are right-associative
  val aList = List(1,2,3)
  val aPrependedList = 0 :: aList // aList.::(0)
  val aBigList = 0 :: 1 :: 2 :: 3 :: List(4,5) // List(4,5).::(3).::(2).::(1).::(0)

  class MySteam[T] {
    infix def -->:(value: T): MySteam[T] = this // impl not important
  }

  val mySteam = 1 -->: 2 -->: 3 -->: 4 -->: new MySteam[Int]

  // 4 - multi-word identifiers
  class Talker(name: String) {
    infix def 'and then said'(gossip: String) = println(s"$name said $gossip")
  }

  def main(args: Array[String]): Unit = {

  }
}
")
file://<WORKSPACE>/src/main/scala/com/rockthejvm/part1as/DarkSugars.scala
file://<WORKSPACE>/src/main/scala/com/rockthejvm/part1as/DarkSugars.scala:63: error: expected identifier; obtained symbollit
    infix def 'and then said'(gossip: String) = println(s"$name said $gossip")
              ^
#### Short summary: 

expected identifier; obtained symbollit