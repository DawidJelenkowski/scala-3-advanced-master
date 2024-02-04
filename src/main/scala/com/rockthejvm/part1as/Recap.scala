package com.rockthejvm.part1as

import scala.annotation.tailrec
object Recap {
  
  // values, types and expressions
  val aCondition = false // vals are constants
  val anIfExpression = if (aCondition) 42 else 55 // expressions evaluate to a value

  val aCodeBlock = {
    if (aCondition) 74
    56
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
  
  def main(args: Array[String]): Unit = {

  }
}
