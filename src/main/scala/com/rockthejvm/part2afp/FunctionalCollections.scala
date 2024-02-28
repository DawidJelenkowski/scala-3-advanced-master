package com.rockthejvm.part2afp

import scala.collection

object FunctionalCollections {
  
  val aSet: Set[String] = Set("I", "love", "Scala")

  // is the same
  // val setContainsScala = aSet.contains("Scala") // true
  // as
  val setContainsScala = aSet("Scala") // true
  // because of the apply method in the Set trait

  // Seq 
  // Seq are Partial functions (index => element) 
  val aSeq: Seq[Int] = Seq(1,2,3,4)
  val anElement = aSeq(2)
  // val aNonExistingElement = aSeq(100) // throws an IndexOutOfBoundsException

  // Map[K, V] "extends" PartialFunction[K, V]
  val aPhonebook: Map[String, Int] = Map(
    "Alice" -> 123456,
    "Bob" -> 987654,
  )
  val alicePhone = aPhonebook("Alice")
  // val danielsPhone = aPhonebook("Daniel") // throws a NoSuchElementException

  def main(args: Array[String]): Unit = {
    println(anElement)
  }
}
