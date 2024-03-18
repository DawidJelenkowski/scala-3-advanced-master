package com.rockthejvm.part2afp

object LazyEvaluation {
  // the expression is evaluatet first and then assigned to x
  // thats why it will print "Hello"
  // val x: Int = {
  //   println("Hello")
  //   42
  // }


  // lazy delays the evaluation of the expression until the first time it is used
  // evaluation occurs ONCE

  lazy val x: Int = {
    println("Hello")
    42
  }
  // Example 1: call by need
  def byNameMethod(n: => Int): Int = 
    n + n + n + 1

  def retrieveMagicValue() = {
    println("waiting")
    Thread.sleep(1000)
    42
  }

  def demoByName(): Unit = {
    println(byNameMethod(retrieveMagicValue()))
    // retrieveMagicValue() + retrieveMagicValue() + retrieveMagicValue() + 1
  }

  // call by need = call by name + lazy values
  def byNeedMethod(n: => Int): Int = {
    lazy val lazyN = n // memoization
    lazyN + lazyN + lazyN + 1
  }

  def demoByNeed(): Unit = {
    println(byNeedMethod(retrieveMagicValue()))
  }

  // Example 2: withFilter
  def lessThan30(i: Int): Boolean = {
    println(s"$i is less than 30?")
    i < 30
  }

  def greaterThan20(i: Int): Boolean = {
    println(s"$i is greater than 20?")
    i > 20
  }

  val numbers = List(1, 25, 40, 5, 23)

  def demoFilter(): Unit = {
    val lt30 = numbers.filter(lessThan30)
    val gt20 = lt30.filter(greaterThan20)
    println(gt20)
  }

  // withFilter is a lazy version of filter
  // it takes a predicate and returns a new collection that is lazy
  // look at the order of the printlns
  def demoWithFilter(): Unit = {
    val lt30 = numbers.withFilter(lessThan30)
    val gt20 = lt30.withFilter(greaterThan20)
    // println(gt20.map(x => x))
    println(gt20.map(identity))
  }

  // comprehensions use lazy evaluation and generators by default
  def demoForComprehension(): Unit = {
    val forComp = for {
      n <- numbers if lessThan30(n) && greaterThan20(n)
    } yield n
    println(forComp)
  }

  def main(args: Array[String]): Unit = {
    println(x)
    println(x)
    /* output
    Hello
    42
    42 
    */
    demoByName()
    /* output
    waiting
    waiting
    waiting
    127
    */
    demoFilter()
    println("-" * 100)
    demoWithFilter()
    println("-" * 100)
    demoForComprehension()
  }
}
