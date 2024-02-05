package com.rockthejvm.part1as

import scala.annotation.tailrec
object Recap {
  
  // values, types and expressions
  val aCondition = false // vals are constants
  val anIfExpression = if (aCondition) 42 else 55 // expressions evaluate to a value

  val aCodeBlock = {
    if (aCondition) 74 
    else 56
  }

  // types: Int, String, Boolean, Char, Double, Float, Long, Short, Byte
  // Unit = () == void in other languages
  val theUnit = println("Hello, Scala") // the value of theUnit is ()

  // functions
  def aFunction(x: Int): Int = x + 1

  // recursion: stack and tail
  @tailrec
  def factorial(n: Int, acc: Int): Int =
    if (n <= 0) acc
    else factorial(n - 1, n * acc)

  val fact10 = factorial(10, 1)

  // object-oriented programming
  class Animal
  class Dog extends Animal
  val aDog: Animal = new Dog // polymorphism
  
  trait Carnivore {
    infix def eat(a: Animal): Unit
  }
  
  class Crocodile extends Animal with Carnivore {
    override infix def eat(a: Animal): Unit = println("I'm eating you, animal!")
  }
  
  val aCroc = new Crocodile
  aCroc.eat(aDog)
  aCroc eat aDog // infix notation = object method argument

  // anonymous classes
  val aCarnivore = new Carnivore {
    override infix def eat(a: Animal): Unit = println("I'm eating you, anonymous animal!")
  }

  aCarnivore.eat(aDog)
  aCarnivore eat aDog

  //generics
  abstract class LList[A] {
    // type A is known inside the implementation
  }
  // singletone and companions
  object LList // companion object, used for instance-independent ("static") fields/methods

  // case classes
  case class Person(name: String, age: Int)

  // enums
  enum BasicColors {
    case RED, GREEN, BLUE
  }

  // exceptions and try-catch-finally
  def throwSomeException(): Int =
    throw new RuntimeException

  val aPotentialFailure = try {
    // code that might fail
    throwSomeException()
  } catch {
    case e: Exception => "I caught an exception!"
  } finally {
    // closing resources
    println("some important logs")
  }

  // functional programming
  val incrementer = new Function1[Int, Int] {
    override def apply(x: Int): Int = x + 1
  }

  val two = incrementer(1) // 2
  println(two)

  // lambdas
  val anonymousIncrementer = (x: Int) => x + 1

  // hofs = higher-order functions
  val anIncrementerList = List(1,2,3).map(anonymousIncrementer) // List(2,3,4)
  // map, flatMap, filter

  // for-comprehensions
  val pairs = for {
    number <- List(1,2,3) // for each number
    char <- List('a', 'b') // for each char
  } yield s"$number-$char" // List("1-a", "1-b", "2-a", "2-b", "3-a", "3-b")

  println(pairs)

  // Scala collections: Seqs, Arrays, Lists, Vectors, Maps, Tuples

  // Option and Try
  val anOption: Option[Int] = Option(42) // Some(42) or None

  println(anOption.map(_ * 2)) // Some(84)

  val x = 2
  val order = x match {
    case 1 => "first"
    case 2 => "second"
    case _ => "not important"}

  val bob = Person("Bob", 22)
  val greeting = bob match {
    case Person(n, _) => s"Hi, my name is $n"
  }

  // braceless syntax
  val pairs_v2 = 
    for
      number <- List(1,2,3)
      char <- List('a', 'b')
    yield s"$number-$char"
    // same for if, match, while

  // indentation tokens
  class BracelessAnimal:
    def eat(): Unit = {
      println("I'm eating")
      println("I'm still eating")
    }
    end eat
  end BracelessAnimal
  def main(args: Array[String]): Unit = {

  }
}
