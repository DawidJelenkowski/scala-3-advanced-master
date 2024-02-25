package com.rockthejvm.part2afp

object PartialFunctions {
  
  val aFunction: Int => Int = x => x + 1

  val aFussyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new RuntimeException("no suitable cases possible")

  val aFussyFunction_v2 = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }

  // partial function
  val aPartialFunction: PartialFunction[Int, Int] = { // x => x match { ... }
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }

  // Checks if a value is contained in the function's domain.
  val canCallOn37 = aPartialFunction.isDefinedAt(37)

  // Turns this partial function into a plain function returning an Option result.
  val liftedPF = aPartialFunction.lift // Int => Option[Int]

  val anotherPF: PartialFunction[Int, Int] = {
    case 45 => 86
  }

  // chaining partial functions
  val pfChain = aPartialFunction.orElse[Int, Int](anotherPF)

  // HOFs accept PFs as arguments
  val aList = List(1,2,3,4)
  val aChangedList = aList.map(x => x match {
    case 1 => 4
    case 2 => 3
    case 3 => 45
    case 4 => 67
    case _ => 0
  })

  val aChangedList_v2 = aList.map({ // possible because PartialFunction[A, B] extends Function1[A, B]
    case 1 => 4
    case 2 => 3
    case 3 => 45
    case 4 => 67
    case _ => 0
  })

  // brackets are optional
  val aChangedList_v3 = aList.map{
    case 1 => 4
    case 2 => 3
    case 3 => 45
    case 4 => 67
    case _ => 0
  }

  case class Person(name: String, age: Int)
  val someKids = List(
    Person("Alice", 12),
    Person("Bob", 7),
    Person("Charlie", 15)
  )

  val kidsGrowingUp = someKids.map({
    case Person(name, age) => Person(name, age + 1)
  })

  def main(args: Array[String]): Unit = {
    println(liftedPF(5)) // Some(999)
    println(liftedPF(37)) // it doesn't crash
    println(canCallOn37) // None
    println(pfChain(45)) // 86
    println(kidsGrowingUp)
  }
}
