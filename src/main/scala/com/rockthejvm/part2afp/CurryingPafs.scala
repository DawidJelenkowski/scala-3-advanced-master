package com.rockthejvm.part2afp

object CurryingPafs {
  
  // currying
  val superAdder: Int => Int => Int =
    x => y => x + y

  val add3: Int => Int = superAdder(3) // y => 3 + y
  val eight = add3(5) // 8
  val eight_v2 = superAdder(3)(5) // 8

  // curreid methods
  def curriedAdder(x: Int)(y: Int): Int =
    x + y
  
  // methods != function values
  
  // converting methods to functions = eta-expansion
  val add4 = curriedAdder(4)
  val nine = add4(5) // 9

  def increment(x: Int) = x + 1
  val aList = List(1,2,3)
  val anIncrementedList = aList.map(increment) // eta-expansion

  // underscores are powerful
  def concatenator(a: String, b: String, c: String): String = a + b + c
  val insertName = concatenator(
    "Hello, I'm ",
    _: String,
    ", how are you?") // x => concatenator("Hello, I'm ", x, ", how are you?")

  val greeting = insertName("Daniel") // "Hello, I'm Daniel, how are you?"

  val fillInTheBlanks = concatenator(_: String, "Daniel", _: String) // (x, y) => concatenator(x, "Daniel", y)
  val danielsGreeting = fillInTheBlanks("Hi, ", " how are you?") // "Hi, Daniel, how are you?

  /**
    * Exercises:
    *
    * @param args
    */
  
  val simpleAddFunction = (x: Int, y: Int) => x + y
  def simpleAddMethod(x: Int, y: Int) = x + y
  def curriedMethod(x: Int)(y: Int) = x + y

  // 1 - obtain an add7 function: x => x + 7 out of these 3 definitions
  val add7 = (x: Int) => simpleAddFunction(x, 7)
  val add7_v2 = (x: Int) => simpleAddMethod(x, 7)
  val add7_v3 = (x: Int) => curriedMethod(7)(x)
  val add7_v4 = curriedMethod(7)
  val add7_v5 = simpleAddMethod(7, _)
  val add7_v6 = simpleAddMethod(_, 7)
  val add7_v7 = simpleAddFunction.curried(7)

  // 2 - process a list of numbers and return their string representations with different formats
  // step 1: create a curried formatting method with a formatting string and a value
  def curriedFormatter(fmt: String)(number: Double): String = fmt.format(number)

  // step 2: process a list of numbers with various formats
  val piWith2Dec = "%8.6f".format(Math.PI) // 3.141593
  val someDecimals = List(Math.PI, Math.E, 1, 9.8, 1.3e-13)

  // methods vs functions + by-name vs 0-lambdas
  def byName(n: => Int) = n + 1
  def byLambda(n: () => Int) = n() + 1

  def method: Int = 42
  def parenMethod(): Int = 42

  byName(23) // ok
  byName(method) // 43. eta-expanded? NO - method is INVOKED here
  byName(parenMethod()) // 43. eta-expanded? YES
  // byName(parenMethod()) // not ok
  byName((() => 42)()) // ok
  // byName(() => 42) // not ok

  // byLambda(23) // not ok
  // byLambda(method) // eta-expansion is NOT possible
  byLambda(parenMethod) // eta-expansion is done
  byLambda(() => 42) // ok
  byLambda(() => parenMethod()) // ok
  
  def main(args: Array[String]): Unit = {
    println(someDecimals.map(curriedFormatter("%4.2f")))
    println(someDecimals.map(curriedFormatter("%8.6f")))
    println(someDecimals.map(curriedFormatter("%16.14f")))
  }
}
